package cn.itcast.travel.service;

public interface FavouriteService {
    boolean isFavourite(String rid,int uid);

    void add(String rid, int uid);
}
