package com.briup.service.impl;

import com.briup.bean.User;
import com.briup.dao.IUserDao;
import com.briup.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class IUserServiceImpl implements IUserService {
    @Autowired
    private IUserDao userDao;
    @Override
    public User login(String name, String password) throws Exception {
        User user=userDao.findByLoginName(name);
        System.out.println(user.getPasswordMd5()+"--"+DigestUtils.md5DigestAsHex(password.getBytes()));
        if(user==null){
            throw  new Exception("用户名不存在");
        }else if(user.isLock()){
            throw new Exception("用户被锁，请联系管理员");
        }else if(!user.getPasswordMd5().equals(DigestUtils.md5DigestAsHex(password.getBytes()))){
            throw new Exception("密码不正确");
        }
        return user;
    }

    @Override
    public void register(User user) throws Exception {
        User u=userDao.findByLoginName(user.getLoginName());
        if(u==null){
            user.setPasswordMd5(DigestUtils.md5DigestAsHex(user.getPasswordMd5().getBytes()));
            userDao.saveUser(user);
        }else{
            throw new Exception("用户名存在");
        }
    }
}
