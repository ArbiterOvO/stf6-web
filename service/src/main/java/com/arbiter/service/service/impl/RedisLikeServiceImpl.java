package com.arbiter.service.service.impl;

import com.arbiter.common.enums.LikedStatusEnum;
import com.arbiter.common.util.RedisLikeUtil;
import com.arbiter.service.pojo.dto.LikedCountDTO;
import com.arbiter.service.pojo.po.PostLike;
import com.arbiter.service.service.RedisLikeService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class RedisLikeServiceImpl implements RedisLikeService {

    private final RedisTemplate<String,Object> redisTemplate;

    /**
     * redis有
     * @param likedUserId
     * @param likedPostId
     */
    @Override
    public void saveLiked2Redis(String likedUserId, String likedPostId) {
//        boolean isLiked = checkIfLiked(likedUserId, likedPostId);
//        if (isLiked)
//        {
//            redisTemplate.opsForHash().put(RedisLikeUtil.MAP_KEY_USER_LIKED, key, LikedStatusEnum.UNLIKE.getCode());
//            decrementLikedCount(likedPostId);
//        }
        String key = RedisLikeUtil.getLikedKey(likedUserId, likedPostId);
        redisTemplate.opsForHash().put(RedisLikeUtil.MAP_KEY_USER_LIKED, key, LikedStatusEnum.LIKE.getCode());
        incrementLikedCount(likedPostId);

    }

    @Override
    public void unlikeFromRedis(String likedUserId, String likedPostId) {
        String key = RedisLikeUtil.getLikedKey(likedUserId, likedPostId);
        redisTemplate.opsForHash().put(RedisLikeUtil.MAP_KEY_USER_LIKED, key, LikedStatusEnum.UNLIKE.getCode());
        decrementLikedCount(likedPostId);
    }

    @Override
    public boolean checkIfLiked(String likedUserId, String likedPostId) {
        String key = RedisLikeUtil.getLikedKey(likedUserId, likedPostId);
        Integer o = (Integer) redisTemplate.opsForHash().get(RedisLikeUtil.MAP_KEY_USER_LIKED, key);
        if (o==null||o.intValue()==LikedStatusEnum.UNLIKE.getCode())
            return false;
        else
            return true;
    }

    @Override
    public void deleteLikedFromRedis(String likedUserId, String likedPostId) {
        String key = RedisLikeUtil.getLikedKey(likedUserId, likedPostId);
        redisTemplate.opsForHash().delete(RedisLikeUtil.MAP_KEY_USER_LIKED, key);
    }

    @Override
    public void incrementLikedCount(String postId) {
        redisTemplate.opsForHash().increment(RedisLikeUtil.MAP_KEY_USER_LIKED_COUNT, postId, 1);
    }

    @Override
    public void decrementLikedCount(String postId) {
        redisTemplate.opsForHash().increment(RedisLikeUtil.MAP_KEY_USER_LIKED_COUNT, postId, -1);
    }

    @Override
    public Integer getLikedCount(Integer postId) {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisLikeUtil.MAP_KEY_USER_LIKED_COUNT, ScanOptions.NONE);
        while (cursor.hasNext()){
            Map.Entry<Object, Object> map = cursor.next();
            Integer count = (Integer)map.getValue();
            if (map.getKey().equals(postId.toString()))
            return count;
        }
        return 0;
    }

    @Override
    public List<PostLike> getLikedDataFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisLikeUtil.MAP_KEY_USER_LIKED, ScanOptions.NONE);
        List<PostLike> list = new ArrayList<>();
        while (cursor.hasNext()){
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            //分离出 likedUserId，likedPostId
            String[] split = key.split("::");
            String likedUserId = split[0];
            String likedPostId = split[1];
            Integer value = (Integer) entry.getValue();

            //组装成 UserLike 对象
            PostLike userLike = new PostLike();
            userLike.setUserId(Integer.parseInt(likedUserId));
            userLike.setPostId(Integer.parseInt(likedPostId));
            userLike.setLikedStatus(value);
            list.add(userLike);

            //存到 list 后从 Redis 中删除
            redisTemplate.opsForHash().delete(RedisLikeUtil.MAP_KEY_USER_LIKED, key);
        }

        return list;
    }

    @Override
    public List<LikedCountDTO> getLikedCountFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisLikeUtil.MAP_KEY_USER_LIKED_COUNT, ScanOptions.NONE);
        List<LikedCountDTO> list = new ArrayList<>();
        while (cursor.hasNext()){
            Map.Entry<Object, Object> map = cursor.next();
            //将点赞数量存储在 LikedCountDT
            String key = (String)map.getKey();
            LikedCountDTO dto = new LikedCountDTO();
            dto.setPostId(Integer.parseInt(key));
            dto.setLikedCount((Integer) map.getValue());
            list.add(dto);
            //从Redis中删除这条记录
            redisTemplate.opsForHash().delete(RedisLikeUtil.MAP_KEY_USER_LIKED_COUNT, key);
        }
        return list;
    }
}
