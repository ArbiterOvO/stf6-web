package com.arbiter.service.pojo.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostDTO {
    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 作者ID
     */
    private Integer authorId;

    /**
     * 标签IDs
     */
    private List<Integer> tagIds;

    /**
     * 图片地址
     */
    private List<String> imgs;
}
