package com.mr.service;

import com.mr.mapper.UserMapper;
import com.mr.model.TMallUserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Lmy on 2018/11/5.
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    public TMallUserAccount findUserNamePswd(String userName, String password) {
        return userMapper.findUserNamePswd(userName,password);
    }
}
