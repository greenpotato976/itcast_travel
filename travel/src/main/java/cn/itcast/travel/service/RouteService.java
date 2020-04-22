package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

public interface RouteService {

    PageBean<Route> pageQuery(String currentPageStr, String pageSizeStr, String cidStr,String rname);

    Route findOne(int rid);
}
