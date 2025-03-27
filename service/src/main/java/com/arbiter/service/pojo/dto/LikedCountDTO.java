package com.arbiter.service.pojo.dto;

import lombok.Data;

@Data
public class LikedCountDTO {
    /**
     * 帖子id
     */
    private Integer postId;
    /**
     * 点赞数
     */
    private Integer likedCount;
}
