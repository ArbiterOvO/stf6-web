package com.arbiter.service.controller;

import com.arbiter.common.po.User;
import com.arbiter.common.result.Result;
import com.arbiter.common.util.ThreadLocalUtil;
import com.arbiter.service.pojo.dto.PageSearchDTO;
import com.arbiter.service.pojo.dto.PostDTO;
import com.arbiter.service.pojo.dto.PostLikeDTO;
import com.arbiter.service.pojo.po.Post;
import com.arbiter.service.pojo.vo.PostDetailVO;
import com.arbiter.service.pojo.vo.PostVO;
import com.arbiter.service.repository.RedisViewRepository;
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
    private RedisViewRepository reViewRepository;

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

    @PutMapping("/view/{postId}")
    public Result<String> View(@PathVariable Integer postId)
    {
        reViewRepository.incrementViewCount(postId);
        return Result.success();
    }

    @PutMapping("/like/{postId}")
    public Result<String> like(@PathVariable Integer postId) {
        User currentUser = ThreadLocalUtil.getCurrentUser();
        redisLikeService.saveLiked2Redis(currentUser.getId().toString(),postId.toString());
        return Result.success("点赞成功");
    }

    @PutMapping("/unlike/{postId}")
    public Result<String> unLike(@PathVariable Integer postId) {
        User currentUser = ThreadLocalUtil.getCurrentUser();
        redisLikeService.unlikeFromRedis(currentUser.getId().toString(),postId.toString());
        return Result.success("取消点赞成功");
    }
}
