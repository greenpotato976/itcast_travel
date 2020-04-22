package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavouriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavouriteServiceImpl;
import cn.itcast.travel.service.impl.RouteSericeImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService routeSerice = new RouteSericeImpl();
    private FavouriteService favouriteService = new FavouriteServiceImpl();
    /**
     * 分页查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取参数
        String currentPageStr = request.getParameter("currentPage");//当前页码
        String pageSizeStr = request.getParameter("pageSize");//每页显示条目数
        String cidStr = request.getParameter("cid");//类别id
        String rname = request.getParameter("rname");
        rname = new String(rname.getBytes("iso-8859-1"),"utf-8");
        //2.调用方法查询pageBean对象
        PageBean<Route> pb = routeSerice.pageQuery(currentPageStr,pageSizeStr,cidStr,rname);
        //3.将pageBean序列化为json响应
        writeValue(pb,response);


    }
    /**
     * 查询一个路线对象，返回为json
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //获取参数rid
        String rid = request.getParameter("rid");
        //获取Route对象
        Route route = routeSerice.findOne(Integer.parseInt(rid));
        //序列化为json返回
        writeValue(route,response);
    }

    /**
     * 获取是否收藏标记
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavourite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //1.获取参数，获取当前登陆的用户
        String rid = request.getParameter("rid");
        //2.获取当前登陆的用户
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int uid = 0;
        if(user != null){
            //用户已经登陆
            uid = user.getUid();
        }
        //2.调用FavouriteService方法获取标记
        boolean flag = favouriteService.isFavourite(rid, uid);
        //3.将标记写回
        writeValue(flag,response);
    }

    /**
     * 添加收藏信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //1.获取参数，获取当前登陆的用户
        String rid = request.getParameter("rid");
        //2.获取当前登陆的用户
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int uid;
        if(user != null){
            //用户已经登陆
            uid = user.getUid();
        } else{
            return;
        }
        //3.将信息添加到数据库
        favouriteService.add(rid,uid);
    }
}
