package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

public interface UserService {
    /**
     * 注册方法
     * @param registerUser 注册用户对象
     * @return 成功返回true 失败返回false
     */
    boolean register(User registerUser);

    /**
     * 用户激活方法
     * @param code 激活码
     * @return 激活状态
     */
    boolean active(String code);

    /**
     * 用户登录方法
     * @param loginUser 前端登录封装的用户对象
     * @return 包含全部信息的User对象
     */
    User login(User loginUser);
}
