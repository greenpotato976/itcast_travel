package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public User findUserByUsername(User registerUser) {
        User user = null;
        try{
            //定义sql
            String sql = "select * from tab_user where username = ?";
            //执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), registerUser.getUsername());
        } catch (Exception e){
        }

        return user;
    }

    @Override
    public void save(User registerUser) {
        //定义sql
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values (?,?,?,?,?,?,?,?,?)";
        //执行sql
        template.update(sql,
                registerUser.getUsername(),
                registerUser.getPassword(),
                registerUser.getName(),
                registerUser.getBirthday(),
                registerUser.getSex(),
                registerUser.getTelephone(),
                registerUser.getEmail(),
                registerUser.getStatus(),
                registerUser.getCode());
    }

    @Override
    public User findUserByCode(String code) {
        User user = null;
        try{
            //定义sql
            String sql = "select * from tab_user where code = ?";
            //执行sql
            user = template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),code);
        } catch (Exception e){

        }
        return user;
    }

    @Override
    public void updateStatus(User user) {
        //定义sql
        String sql = "update tab_user set status = 'Y' where uid = ?";
        //执行sql
        template.update(sql,user.getUid());
    }

    @Override
    public User findUserByUsernameAndPassword(String username, String password) {
        User user = null;
        try{
            //定义sql
            String sql = "select * from tab_user where username = ? and password = ?";
            //执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class),username,password);
        } catch (Exception e){
        }

        return user;
    }
}
