package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {
    /**
     * 根据cid查询总记录数
     */
    int findTotalCount(int cid,String rname);

    /**
     * 根据start,pageSize,cid查询Route集合
     */
    List<Route> findByPage(int cid,int start,int pageSize,String rname);

    /**
     * 通过rid查询ROute对象
     * @param rid
     * @return
     */
    Route findOne(int rid);


}
