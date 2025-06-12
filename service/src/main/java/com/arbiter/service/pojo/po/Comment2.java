package com.arbiter.service.pojo.po;

import com.arbiter.service.pojo.BaseEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@TableName("comment_comment")
public class Comment2 extends BaseEntity {

    /**
     * 内容
     */
    private String content;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 评论id
     */
    private Integer commentId;

    /**
     * 点赞数
     */
    private Integer likeNum;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT,exist = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
