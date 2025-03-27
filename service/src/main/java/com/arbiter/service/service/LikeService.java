package com.arbiter.service.service;

import com.arbiter.service.pojo.po.PostLike;

import java.util.List;

public interface LikeService{
    /**
     * 保存点赞记录
     * @param userLike
     * @return
     */
    PostLike save(PostLike userLike);

    /**
     * 批量保存或修改
     * @param list
     */
    List<PostLike> saveAll(List<PostLike> list);


    /**
     * 通过被点赞人和帖子id查询是否存在点赞记录
     */
    PostLike getByLikedUserIdAndLikedPostId(Integer userId, Integer postId);

    /**
     * 将Redis里的点赞数据存入数据库中
     */
    void transLikedFromRedis2DB();

    /**
     * 将Redis中的点赞数量数据存入数据库
     */
    void transLikedCountFromRedis2DB();
}
