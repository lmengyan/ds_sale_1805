package com.mr.service;

import com.mr.model.TMallUserAccount;

/**
 * Created by Lmy on 2018/11/5.
 */
public interface UserService {
    TMallUserAccount findUserNamePswd(String userName, String password);
}
