package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavouriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavouriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImple;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteSericeImpl implements RouteService {
    private RouteDao dao = new RouteDaoImple();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavouriteDao favouriteDao = new FavouriteDaoImpl();
    /**
     * 查询pageBean对象方法
     * @param currentPageStr 当前页码字符窜
     * @param pageSizeStr 每页显示数量字符串
     * @param cidStr 类别id字符串
     * @return 返回pageBean对象
     */
    @Override
    public PageBean<Route> pageQuery(String currentPageStr, String pageSizeStr, String cidStr,String rname) {
        int currentPage = 0;//当前页码
        int pageSize = 0;//每页显示条数
        int cid = 0;//类别id
        //1.处理参数，设置初始值
        if(currentPageStr != null && currentPageStr.length() > 0){
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }
        if(pageSizeStr != null && pageSizeStr.length() > 0){
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 5;
        }
        if(cidStr != null && cidStr.length() >0 ){
            cid = Integer.parseInt(cidStr);
        }
        //2.封装pageBean对象
        PageBean<Route> pb = new PageBean<>();
        //2.1设置当前页码
        pb.setCurrentPage(currentPage);
        //2.2设置每页显示条数
        pb.setPageSize(pageSize);
        //2.3设置总记录数
        int totalCount = dao.findTotalCount(cid,rname);
        pb.setTotalCount(totalCount);
        //2.4设置总页数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount/pageSize) + 1;
        pb.setTotalPage(totalPage);
        //2.5设置Route集合
        int start = (currentPage - 1) * pageSize;
        List<Route> list = dao.findByPage(cid,start,pageSize,rname);
        pb.setList(list);
        return pb;
    }

    /**
     * 查询一个包含详细信息的Route对象
     * @param rid 路线id
     * @return 包含详细信息的Route对象
     */
    @Override
    public Route findOne(int rid) {
        //1.根据rid查询一个Route对象
        Route route = dao.findOne(rid);
        //2.封装Route对象
        //2.1设置商品详情图片列表List<RouteImg>
        List<RouteImg> routeImgs = routeImgDao.findAll(rid);
        route.setRouteImgList(routeImgs);
        //2.2设置所属商家Seller
        Seller seller = sellerDao.findByid(route.getSid());
        route.setSeller(seller);
        //3.设置收藏次数
        int count = favouriteDao.findCountByRid(rid);
        route.setCount(count);
        return route;
    }
}
