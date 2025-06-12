package com.arbiter.service.controller;

import com.arbiter.common.po.User;
import com.arbiter.common.result.Result;
import com.arbiter.common.util.ThreadLocalUtil;
import com.arbiter.service.pojo.dto.Comment2DTO;
import com.arbiter.service.pojo.po.Comment;
import com.arbiter.service.pojo.po.Comment2;
import com.arbiter.service.pojo.vo.Comment2VO;
import com.arbiter.service.pojo.vo.CommentVO;
import com.arbiter.service.service.Comment2Service;
import com.arbiter.service.service.CommentService;
import com.arbiter.service.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;
    private final UserService userService;
    private final Comment2Service comment2Service;

    @PostMapping("/add")
    public Result<CommentVO> addComment(@RequestBody Comment comment) {
        System.out.println("comment = " + comment);
        commentService.save(comment);
        CommentVO commentVO = new CommentVO();
        User user = userService.getById(comment.getUserId());
        commentVO.setUserAvatar(user.getHeadImg());
        commentVO.setUserName(user.getUsername());
        commentVO.setUserId(comment.getUserId());
        commentVO.setContent(comment.getContent());
        commentVO.setCreateTime(comment.getCreateTime());
        return Result.success(commentVO);
    }

    @PutMapping("/like/{commentId}")
    public Result<String> likeComment(@PathVariable Integer commentId) {
        User currentUser = ThreadLocalUtil.getCurrentUser();
        commentService.likeComment(commentId,currentUser.getId());
        return Result.success();
    }

    @PutMapping("/unlike/{commentId}")
    public Result<String> unlikeComment(@PathVariable Integer commentId) {
        User currentUser = ThreadLocalUtil.getCurrentUser();
        commentService.unlikeComment(commentId,currentUser.getId());
        return Result.success();
    }

    @PostMapping("/comment2/add")
    public Result<Comment2VO> getComment(@RequestBody Comment2DTO comment2DTO) {
        User currentUser = ThreadLocalUtil.getCurrentUser();
        Comment2 comment2=new Comment2();
        comment2.setCommentId(comment2DTO.getCommentId());
        comment2.setUserId(currentUser.getId());
        comment2.setContent(comment2DTO.getContent());
        comment2Service.save(comment2);
        Comment2VO comment2VO = new Comment2VO();
        comment2VO.setId(comment2VO.getId());
        comment2VO.setContent(comment2DTO.getContent());
        comment2VO.setCreateTime(comment2.getCreateTime());
        comment2VO.setUserId(comment2.getUserId());
        comment2VO.setCommentId(comment2.getCommentId());
        comment2VO.setUserName(currentUser.getUsername());
        comment2VO.setUserAvatar(currentUser.getHeadImg());
        return Result.success(comment2VO);
    }
}
