package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    /**
     * 判断用户名是否重复
     * @param registerUser 注册用户对象
     * @return 重复返回不为null 不重复返回null
     */
    User findUserByUsername(User registerUser);

    /**
     * 保存方法
     * @param registerUser 注册用户对象
     */
    void save(User registerUser);

    /**
     * 根据激活码查询用户对象
     * @param code 激活码
     * @return 完整信息的用户对象
     */
    User findUserByCode(String code);

    /**
     * 更新用户激活状态方法
     * @param user 完整信息的用户对象
     */
    void updateStatus(User user);

    /**
     * 根据用户名和密码查询数据库对象
     * @param username 登录用户名
     * @param password 登录密码
     * @return 包含全部信息的User对象
     *  null：用户名或密码错误无查询结果
     */
    User findUserByUsernameAndPassword(String username, String password);
}
