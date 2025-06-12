package com.arbiter.service.service.impl;

import com.arbiter.common.enums.LikedStatusEnum;
import com.arbiter.service.mapper.CommentLikeMapper;
import com.arbiter.service.pojo.po.CommentLike;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.arbiter.service.pojo.po.Comment;
import com.arbiter.service.service.CommentService;
import com.arbiter.service.mapper.CommentMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
* @author Arbiter
* @description 针对表【comment】的数据库操作Service实现
* @createDate 2025-03-25 20:22:22
*/
@Service
@AllArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

    private final CommentMapper commentMapper;
    private final CommentLikeMapper commentLikeMapper;
    @Override
    public void likeComment(Integer commentId, Integer userId) {
        CommentLike commentLike = new CommentLike();
        commentLike.setCommentId(commentId);
        commentLike.setUserId(userId);
        commentLike.setLikedStatus(LikedStatusEnum.LIKE.getCode());
        CommentLike findCommentLike = commentLikeMapper.selectOne(new LambdaQueryWrapper<CommentLike>().eq(CommentLike::getCommentId, commentId).eq(CommentLike::getUserId, userId));
        if (findCommentLike != null) {
            commentLikeMapper.update(commentLike,new LambdaQueryWrapper<CommentLike>().eq(CommentLike::getCommentId, commentId).eq(CommentLike::getUserId, userId));
        }
        else {
            commentLikeMapper.insert(commentLike);
        }
        Comment comment = commentMapper.selectById(commentLike.getCommentId());
        comment.setLikeNum(comment.getLikeNum() + 1);
        commentMapper.updateById(comment);

    }

    @Override
    public void unlikeComment(Integer commentId, Integer userId) {
        CommentLike commentLike = new CommentLike();
        commentLike.setCommentId(commentId);
        commentLike.setUserId(userId);
        commentLike.setLikedStatus(LikedStatusEnum.UNLIKE.getCode());
        LambdaQueryWrapper<CommentLike> wrapper = new LambdaQueryWrapper<CommentLike>().eq(CommentLike::getCommentId, commentId);
        wrapper.eq(CommentLike::getUserId, userId);
        commentLikeMapper.update(commentLike,wrapper);
        Comment comment = commentMapper.selectById(commentLike.getCommentId());
        comment.setLikeNum(comment.getLikeNum() - 1);
        commentMapper.updateById(comment);
    }

    @Override
    public boolean checkLikeComment(Integer commentId, Integer userId) {
        LambdaQueryWrapper<CommentLike> eq = new LambdaQueryWrapper<CommentLike>().eq(CommentLike::getCommentId, commentId);
        eq.eq(CommentLike::getUserId, userId);
        CommentLike commentLike = commentLikeMapper.selectOne(eq);
        if (commentLike!=null&& Objects.equals(commentLike.getLikedStatus(), LikedStatusEnum.LIKE.getCode()))
            return true;
        else
            return false;
    }
}




