package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavouriteDao;
import cn.itcast.travel.dao.impl.FavouriteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.service.FavouriteService;

public class FavouriteServiceImpl implements FavouriteService {
    private FavouriteDao favouriteDao = new FavouriteDaoImpl();
    /**
     * 调用dao查询用户是否收藏
     * @param rid
     * @param uid
     * @return 不为null用户有收藏
     */
    @Override
    public boolean isFavourite(String rid, int uid) {
        Favorite favourite = favouriteDao.isFavourite(Integer.parseInt(rid), uid);
        return favourite != null;
    }

    /**
     * 添加信息
     * @param rid
     * @param uid
     */
    @Override
    public void add(String rid, int uid) {
        favouriteDao.add(Integer.parseInt(rid),uid);
    }
}
