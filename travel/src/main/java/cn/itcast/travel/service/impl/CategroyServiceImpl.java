package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategroyDao;
import cn.itcast.travel.dao.impl.CategroyDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategroyService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategroyServiceImpl implements CategroyService {
    private CategroyDao dao = new CategroyDaoImpl();
    @Override
    public List<Category> findAll() {
        //1.从reids中查询数据
        Jedis jedis = JedisUtil.getJedis();
        Set<Tuple> categorys = jedis.zrangeWithScores("category", 0, -1);//查询所有
        //2.进行判断
        List<Category> categoryList = null;
        if(categorys == null || categorys.size() == 0){
            //3.如果为空，将数据库中数据存入redis zadd
            categoryList = dao.findAll();
            for(Category category:categoryList){
                jedis.zadd("category",category.getCid(),category.getCname());
            }
        } else{
            //4.不为空，将redis数据转成List
            categoryList = new ArrayList<Category>();
            for (Tuple tuple : categorys) {
                Category category = new Category();
                category.setCname(tuple.getElement());
                category.setCid((int)tuple.getScore());
                categoryList.add(category);
            }
        }

        return categoryList;
    }
}
