package com.arbiter.service.service;

import com.arbiter.service.pojo.dto.LikedCountDTO;
import com.arbiter.service.pojo.po.PostLike;

import java.util.List;

public interface RedisLikeService {
    /**
     * 点赞。状态为1
     * @param likedUserId
     * @param likedPostId
     */
    void saveLiked2Redis(String likedUserId, String likedPostId);

    /**
     * 取消点赞。将状态改变为0
     * @param likedUserId
     * @param likedPostId
     */
    void unlikeFromRedis(String likedUserId, String likedPostId);

    boolean checkIfLiked(String likedUserId, String likedPostId);

    /**
     * 从Redis中删除一条点赞数据
     * @param likedUserId
     * @param likedPostId
     */
    void deleteLikedFromRedis(String likedUserId, String likedPostId);

    /**
     * 该帖子的点赞数加1
     * @param likedUserId
     */
    void incrementLikedCount(String likedUserId);

    /**
     * 该帖子的点赞数减1
     * @param likedUserId
     */
    void decrementLikedCount(String likedUserId);
    Integer getLikedCount(Integer postId);
    /**
     * 获取Redis中存储的所有点赞数据
     * @return
     */
    List<PostLike> getLikedDataFromRedis();

    /**
     * 获取Redis中存储的所有点赞数量
     * @return
     */
    List<LikedCountDTO> getLikedCountFromRedis();
}
