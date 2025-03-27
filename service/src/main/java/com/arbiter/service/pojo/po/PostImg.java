package com.arbiter.service.pojo.po;

import com.arbiter.service.pojo.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 帖子 图片关联表
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostImg{

    /**
     * 帖子id
     */
    private Integer postId;

    /**
     * 图片URL
     */
    private String img;
}
