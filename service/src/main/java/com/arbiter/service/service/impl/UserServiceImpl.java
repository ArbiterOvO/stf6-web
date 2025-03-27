package com.arbiter.service.service.impl;


import com.arbiter.common.po.User;
import com.arbiter.service.mapper.UserMapper;
import com.arbiter.service.pojo.dto.UserLoginDTO;
import com.arbiter.service.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

    private final UserMapper userMapper;
    @Override
    public void regist(User user) {
        Integer insert = userMapper.insert(user);
    }

    @Override
    public List<User> findAll() {
        return userMapper.selectList(null);
    }

    @Override
    public User login(UserLoginDTO loginDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginDTO.getUsername());
        queryWrapper.eq("password", loginDTO.getPassword());
        return userMapper.selectOne(queryWrapper);
    }
}
