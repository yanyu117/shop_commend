package com.briup.dao;

import com.briup.bean.User;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserDao{
    User findByLoginName(String loginName);
    void saveUser(User user);
}
