package com.arbiter.service.controller;

import com.arbiter.common.po.User;
import com.arbiter.common.result.Result;
import com.arbiter.service.pojo.po.Comment;
import com.arbiter.service.pojo.vo.CommentVO;
import com.arbiter.service.service.CommentService;
import com.arbiter.service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {

    private CommentService commentService;
    private UserService userService;

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
}
