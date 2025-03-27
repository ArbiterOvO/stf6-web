package com.arbiter.service.repository;

import com.arbiter.common.constant.RedisConstant;
import com.arbiter.service.mapper.PostMapper;
import com.arbiter.service.pojo.po.Post;
import com.arbiter.service.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class RedisViewRepository {
    private final RedisTemplate<String,Object> redisTemplate;
    private final PostMapper postMapper;

    public Integer getViewCountById(Integer postId) {
        Object object = redisTemplate.opsForHash().get(RedisConstant.MAP_KEY_Post_VIEW, postId.toString());
        if (object!=null)
            return Integer.parseInt(object.toString());
        else
            return 0;
    }

    public void setViewCount() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisConstant.MAP_KEY_Post_VIEW, ScanOptions.NONE);
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            Integer key = Integer.parseInt(entry.getKey().toString()) ;
            Integer count = Integer.parseInt(entry.getValue().toString()) ;
            Post post = postMapper.selectById(key);
            post.setViewNum(post.getViewNum()+count);
            postMapper.updateById(post);

            redisTemplate.opsForHash().delete(RedisConstant.MAP_KEY_Post_VIEW, key.toString());
        }
    }

    public void incrementViewCount(Integer postId) {
        redisTemplate.opsForHash().increment(RedisConstant.MAP_KEY_Post_VIEW,postId.toString(),1);
    }
}
