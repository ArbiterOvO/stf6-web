package com.arbiter.service.controller;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.arbiter.common.po.User;
import com.arbiter.common.result.Result;
import com.arbiter.common.util.JwtUtil;
import com.arbiter.common.util.ThreadLocalUtil;
import com.arbiter.service.pojo.dto.UserLoginDTO;
import com.arbiter.service.properties.JwtProperties;
import com.arbiter.service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final JwtProperties jwtProperties;
    @GetMapping("/all")
    public Result<List<User>> getAllUser()
    {
        List<User> users = userService.findAll();
        return Result.success(users);
    }

    @PostMapping("/login")
    public Result<JSONObject> login(@RequestBody UserLoginDTO loginDTO){

        User user = userService.login(loginDTO);
        System.out.println(user);
        //登录成功，生产令牌，下发令牌
        if(user != null){
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getId());
            claims.put("username", user.getUsername());
            String token = JwtUtil.createJWT(
                    jwtProperties.getAdminSecretKey(),
                    jwtProperties.getAdminTtl(),
                    claims);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("token", token);
            return Result.success(jsonObject);
        }

        return Result.error("用户名或密码错误");
    }

    @PostMapping("/register")
    public Result<String> register(@RequestBody User user){
        boolean save = userService.save(user);
        return save?Result.success():Result.error("注册失败！");
    }

    @GetMapping("/current")
    public Result<JSONObject> currentUser()
    {
        User currentUser = ThreadLocalUtil.getCurrentUser();
        System.out.println(currentUser);
        JSONObject jsonObject= JSON.parseObject(JSON.toJSONString(currentUser));
        return Result.success(jsonObject);
    }

}
