package com.arbiter.service.pojo.po;

import com.arbiter.service.pojo.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@TableName(value ="post")
@Data
public class Post extends BaseEntity{


    /**
     * 帖子名
     */
    private String title;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 发布人id
     */
    private Integer authorId;

    /**
     * 点赞数
     */
    private Integer likeNum;

    /**
     * 访问数
     */
    private Integer viewNum;

    /**
     * 插入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT,exist = true)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE,exist = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}