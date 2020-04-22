package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface FavouriteDao {
    Favorite isFavourite(int rid,int uid);
    int findCountByRid(int rid);
    void add(int uid,int rid);
}
