package com.arbiter.service.repository;

import cn.hutool.core.util.RandomUtil;
import com.arbiter.common.constant.RedisConstant;
import com.arbiter.service.properties.EmailProperties;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Component
public class EmailRepository {

    private final JavaMailSender javaMailSender;
    private final EmailProperties emailProperties;
    private final RedisTemplate<String, Object> redisTemplate;

    public String emailCode(String userEmail) {

        Integer code = RandomUtil.randomInt(100000, 999999);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(emailProperties.getAddr());
        simpleMailMessage.setTo(userEmail);
        simpleMailMessage.setSubject("论坛邮箱验证码");
        simpleMailMessage.setText("你的验证码是：" + code);
        redisTemplate.opsForHash().put(RedisConstant.MAP_KEY_USER_CODE, userEmail, code);
        redisTemplate.opsForHash().getOperations().expire(RedisConstant.MAP_KEY_USER_CODE,600, TimeUnit.SECONDS);
        javaMailSender.send(simpleMailMessage);
        return code.toString();

    }


    public boolean checkCode(String userEmail,String code) {
        Integer redisCode = (Integer) redisTemplate.opsForHash().get(RedisConstant.MAP_KEY_USER_CODE, userEmail);
        if (redisCode!=null&&redisCode==Integer.parseInt(code)) {
            return true;
        }
        else {
            return false;
        }
    }
}
