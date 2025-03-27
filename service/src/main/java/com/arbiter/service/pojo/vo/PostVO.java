package com.arbiter.service.pojo.vo;

import com.arbiter.service.pojo.po.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 帖子基本情况VO
 */
@Data
public class PostVO {
    /**
     * 帖子Id
     */
    private int postId;
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
     * 作者名字
     */
    private String authorName;
    /**
     * 作者头像
     */
    private String authorAvatar;
    /**
     * 标签IDs
     */
    private List<String> tags;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     *浏览数
     */
    private Integer viewCount;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
