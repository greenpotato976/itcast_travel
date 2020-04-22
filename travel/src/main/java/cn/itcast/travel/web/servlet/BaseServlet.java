package cn.itcast.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*
    完成方法分发
    优化servlet
 */
public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取方法名
        //1.1获取方法名
        String requestURI = request.getRequestURI();//travel/user/login
        String methodName = requestURI.substring(requestURI.lastIndexOf("/") + 1);
        //1.2获取方法对象
        try {
            Method method = this.getClass().getMethod(methodName,HttpServletRequest.class,HttpServletResponse.class);
            //2.通过反射执行方法
            method.invoke(this,request,response);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将数据序列化为json
     * @param obj
     */
    public void writeValue(Object obj,HttpServletResponse response) throws IOException {
        //设置响应类型
        response.setContentType("application/json;charset=utf-8");
        new ObjectMapper().writeValue(response.getOutputStream(),obj);
    }
}
