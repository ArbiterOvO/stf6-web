package com.arbiter.service.controller;

import com.arbiter.common.result.Result;
import com.arbiter.service.pojo.dto.PageSearchDTO;
import com.arbiter.service.pojo.dto.PostDTO;
import com.arbiter.service.pojo.dto.PostLikeDTO;
import com.arbiter.service.pojo.po.Post;
import com.arbiter.service.pojo.vo.PostDetailVO;
import com.arbiter.service.pojo.vo.PostVO;
import com.arbiter.service.service.PostService;
import com.arbiter.service.service.RedisLikeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {

    private PostService postService;
    private RedisLikeService redisLikeService;

    @PostMapping("/add")
    public Result<String> addPost(@RequestBody PostDTO post) {
        boolean res = postService.addPost(post);
        return Result.success();
    }

    @GetMapping("/getById/{id}")
    public Result<PostDetailVO> getPostDetailById(@PathVariable Integer id) {
        PostDetailVO postById = postService.getPostDetailById(id);
        return Result.success(postById);
    }

    @PostMapping("/search")
    public Result<List<PostVO>> SearchPage(@RequestBody PageSearchDTO pageSearchDTO) {
        List<PostVO> allPost = postService.searchPage(pageSearchDTO);
        return Result.success(allPost);
    }

    @PutMapping("/like")
    public Result<String> like(@RequestBody PostLikeDTO postLikeDTO) {

        //Todo 根据redis改造
        redisLikeService.saveLiked2Redis(postLikeDTO.getUserId().toString(),postLikeDTO.getPostId().toString());
        return Result.success("点赞成功");


    }
}
