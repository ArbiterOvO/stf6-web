package com.arbiter.service.controller;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.arbiter.common.po.User;
import com.arbiter.common.result.Result;
import com.arbiter.common.util.JwtUtil;
import com.arbiter.common.util.ThreadLocalUtil;
import com.arbiter.service.pojo.dto.RegisterDTO;
import com.arbiter.service.pojo.dto.UserLoginDTO;
import com.arbiter.service.properties.JwtProperties;
import com.arbiter.service.repository.EmailRepository;
import com.arbiter.service.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final JwtProperties jwtProperties;
    private final EmailRepository emailRepository;

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
    public Result<String> register(@RequestBody RegisterDTO registerDTO){

        String emailCode = registerDTO.getEmailCode();
        log.info("emailCode:{}",registerDTO.getEmail());
        if(emailRepository.checkCode(registerDTO.getEmail(), emailCode))
        {
            User user=new User();
            user.setUsername(registerDTO.getUsername());
            user.setPassword(registerDTO.getPassword());
            user.setEmail(registerDTO.getEmail());
            boolean save = userService.save(user);
            return save?Result.success():Result.error("注册失败！");
        }
        else {
            return Result.error("邮箱验证码错误");
        }

    }

    @GetMapping("/current")
    public Result<JSONObject> currentUser()
    {
        User currentUser = ThreadLocalUtil.getCurrentUser();
        System.out.println(currentUser);
        JSONObject jsonObject= JSON.parseObject(JSON.toJSONString(currentUser));
        return Result.success(jsonObject);
    }

    @PostMapping("/email/{addr}")
    public Result<Integer> postEmail(@PathVariable String addr){
        emailRepository.emailCode(addr);
        return Result.success();
    }


}
