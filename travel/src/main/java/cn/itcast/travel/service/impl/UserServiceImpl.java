package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    private UserDao dao = new UserDaoImpl();
    /**
     * 注册方法
     * @param registerUser 注册用户对象
     * @return 成功返回true 失败返回false
     */
    @Override
    public boolean register(User registerUser) {
        //1.调用findUserByUsername方法，判断用户名是否重复
        User user = dao.findUserByUsername(registerUser);
        //2.重复 返回不为否
        if(user != null){
            //重复
            return false;
        } else{
            //3.不重复调用save方法保存用户,返回true
            registerUser.setCode(UuidUtil.getUuid());
            registerUser.setStatus("N");
            dao.save(registerUser);
            //发送邮件
            String text = "<a href='http://localhost:8080/travel/user/activeUser?code="+registerUser.getCode()+"'>点击激活【黑马旅游网】</a><br>非本人操作请忽略";
            MailUtils.sendMail(registerUser.getEmail(),text,"激活邮箱");
            return true;
        }
    }

    @Override
    public boolean active(String code) {
        //1.判断激活码是否正确，调用findUserByCode
        User user = dao.findUserByCode(code);
        if(user != null){
            //2.更改信息，调用updateStatus
            dao.updateStatus(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User login(User loginUser) {
        return dao.findUserByUsernameAndPassword(loginUser.getUsername(),loginUser.getPassword());
    }
}
