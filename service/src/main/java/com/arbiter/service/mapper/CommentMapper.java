package com.arbiter.service.mapper;

import com.arbiter.service.pojo.po.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Arbiter
* @description 针对表【comment】的数据库操作Mapper
* @createDate 2025-03-25 20:22:22
* @Entity com.arbiter.service.pojo.po.Comment
*/
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}




