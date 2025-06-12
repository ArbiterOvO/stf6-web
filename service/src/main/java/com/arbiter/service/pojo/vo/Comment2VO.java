package com.arbiter.service.pojo.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class Comment2VO {
    /**
     * 2级评论id
     */
    private Integer id;

    /**
     * 内容
     */
    private String content;

    /**
     * 评论id
     */
    private Integer commentId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 是否已经点赞
     */
    private Integer hasLiked;
    /**
     * 点赞数
     */
    private Integer likedNum;
    /**
     * 插入时间
     */
    @TableField(fill = FieldFill.INSERT,exist = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
