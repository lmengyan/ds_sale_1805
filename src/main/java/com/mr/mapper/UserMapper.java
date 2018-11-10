package com.mr.mapper;

import com.mr.model.TMallUserAccount;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Lmy on 2018/11/5.
 */
public interface UserMapper {
    TMallUserAccount findUserNamePswd(@Param("userName") String userName, @Param("password") String password);
}
