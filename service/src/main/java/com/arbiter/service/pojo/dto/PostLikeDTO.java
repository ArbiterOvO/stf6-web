package com.arbiter.service.pojo.dto;

import lombok.Data;

@Data
public class PostLikeDTO {

    /**
     * 帖子id
     */
    private Integer postId;

    /**
     * 用户id
     */
    private Integer userId;
}
