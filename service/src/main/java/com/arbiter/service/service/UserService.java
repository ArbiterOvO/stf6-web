package com.arbiter.service.service;


import com.arbiter.service.pojo.dto.UserLoginDTO;

import com.baomidou.mybatisplus.extension.service.IService;
import com.arbiter.common.po.User;

import java.util.List;


public interface UserService extends IService<User> {
    void regist(User user);

    List<User> findAll();

    User login(UserLoginDTO loginDTO);
}
