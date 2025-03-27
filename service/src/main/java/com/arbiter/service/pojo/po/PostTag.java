package com.arbiter.service.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 帖子 图片关联表
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostTag {
    /**
     * 帖子Id
     */
    private Integer postId;
    /**
     * 标签id
     */
    private Integer tagId;
}
