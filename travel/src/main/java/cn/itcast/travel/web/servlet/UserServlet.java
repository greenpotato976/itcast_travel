package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    private UserService service = new UserServiceImpl();
    /**
     * 用户注册方法
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //对验证码进行判断
        //1.获取验证码
        String check = request.getParameter("check");
        //2.获取session中的验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        //3.忽略大小写比较
        if(checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)){
            //验证码错误
            //4.给提示信息
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //序列化为json
            String json = new ObjectMapper().writeValueAsString(info);
            //响应前台
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }
        //1.获得参数
        Map<String, String[]> userMap = request.getParameterMap();
        //2.封装对象
        User loginUser = new User();
        try {
            BeanUtils.populate(loginUser,userMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //3.调用service方法
        boolean flag = service.register(loginUser);
        //4.给前端响应
        ResultInfo info = new ResultInfo();
        if(flag){
            //注册成功
            info.setFlag(true);
        } else {
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败");
        }
        //序列化为json
        String json = new ObjectMapper().writeValueAsString(info);
        //响应前台
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    /**
     * 用户登录方法
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //验证验证码
        String check = request.getParameter("check");
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        if(!checkcode_server.equalsIgnoreCase(check) && check == null ){
            //验证码错误,给提示信息
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            response.setContentType("application/json;charset=utf-8");
            new ObjectMapper().writeValue(response.getOutputStream(),info);
            return;
        }
        //1.获取参数
        Map<String, String[]> loginMap = request.getParameterMap();
        //2.封装对象
        User loginUser = new User();
        try {
            BeanUtils.populate(loginUser,loginMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //3.调用login方法
        User user = service.login(loginUser);
        //4.判断是否为null
        ResultInfo info = new ResultInfo();
        if(user == null){
            //用户名或密码错误
            info.setErrorMsg("用户名或者密码错误");
            info.setFlag(false);
        }
        //5.判断是否激活
        if(user != null && !"Y".equals(user.getStatus())){
            //用户未激活
            info.setFlag(false);
            info.setErrorMsg("用户未激活，请登录邮箱激活");
        }
        //6.判断登录是否成功
        if(user != null && "Y".equals(user.getStatus())){
            //将登录成功用户存入session中
            request.getSession().setAttribute("user",user);
            //登录成功
            info.setFlag(true);
        }
        //7.响应数据
        response.setContentType("application/json;charset=utf-8");
        new ObjectMapper().writeValue(response.getOutputStream(),info);
    }

    /**
     * 用户激活方法
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void activeUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //1.获得激活码
        String code = request.getParameter("code");
        //2.判断激活码不为空
        if(code !=null){
            //3.调用service完成激活
            boolean flag = service.active(code);
            //给提示信息
            String msg = null;
            if(flag){
                //激活成功
                msg = "激活成功，清<a href='login.html'>登录</a>";
            } else {
                //激活失败
                msg = "激活失败请联系管理人员";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }

    /**
     * 用户推出方法
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //1.销毁session
        request.getSession().invalidate();
        //2.页面重定向到登录页面
        response.sendRedirect(request.getContextPath()+"/login.html");
    }

    /**
     * 从session中获取User对象
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //1.将User对象从session中取出
        Object user = request.getSession().getAttribute("user");
        //2.响应数据
        response.setContentType("application/json;charset=tf-8");
        new ObjectMapper().writeValue(response.getOutputStream(),user);
    }
}
