package com.arbiter.service.pojo.dto;

import lombok.Data;

@Data
public class Comment2DTO {
    /**
     * 回复内容
     */
    private String content;
    /**
     * 评论id
     */
    private Integer commentId;
}
