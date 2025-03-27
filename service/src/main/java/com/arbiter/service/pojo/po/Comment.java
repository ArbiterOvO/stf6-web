package com.arbiter.service.pojo.po;

import com.arbiter.service.pojo.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * @TableName comment
 */
@TableName(value ="comment")
@Data
public class Comment extends BaseEntity {

    /**
     * 内容
     */
    private String content;

    /**
     * 评论表id
     */
    private Integer postId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 插入时间
     */
    @TableField(fill = FieldFill.INSERT,exist = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


}