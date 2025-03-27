package com.arbiter.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.arbiter.service.pojo.po.Comment;
import com.arbiter.service.service.CommentService;
import com.arbiter.service.mapper.CommentMapper;
import org.springframework.stereotype.Service;

/**
* @author Arbiter
* @description 针对表【comment】的数据库操作Service实现
* @createDate 2025-03-25 20:22:22
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

}




