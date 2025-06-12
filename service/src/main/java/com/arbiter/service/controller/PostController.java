package com.arbiter.service.controller;

import cn.hutool.core.bean.BeanUtil;
import com.arbiter.common.po.User;
import com.arbiter.common.result.Result;
import com.arbiter.common.util.ThreadLocalUtil;
import com.arbiter.service.pojo.dto.PageSearchDTO;
import com.arbiter.service.pojo.dto.PostDTO;
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

    /**
     * 添加帖子
     * @param post
     * @return
     */
    @PostMapping("/add")
    public Result<String> addPost(@RequestBody PostDTO post) {

        post.setAuthorId(ThreadLocalUtil.getCurrentUser().getId());
        postService.addPost(post);
        return Result.success();
    }

    @PostMapping("/delete/{id}")
    public Result<String> deletePost(@PathVariable Integer id) {
        postService.removeById(id);
        return Result.success();
    }

    @PostMapping("/update")
    public Result<String> updatePost(@RequestBody PostDTO postDTO) {
        postService.updatePost(postDTO);
        return Result.success();
    }

    /**
     * 通过id获取梯子详情
     * @param id
     * @return
     */
    @GetMapping("/getById/{id}")
    public Result<PostDetailVO> getPostDetailById(@PathVariable Integer id) {
        PostDetailVO postById = postService.getPostDetailById(id);
        return Result.success(postById);
    }

    /**
     * 搜索帖子
     * @param pageSearchDTO
     * @return
     */
    @PostMapping("/search")
    public Result<List<PostVO>> SearchPage(@RequestBody PageSearchDTO pageSearchDTO) {
        List<PostVO> allPost = postService.searchPage(pageSearchDTO);
        return Result.success(allPost);
    }

    /**
     * 浏览帖子 浏览数+1
     * @param postId
     * @return
     */
    @PutMapping("/view/{postId}")
    public Result<String> View(@PathVariable Integer postId)
    {
        reViewRepository.incrementViewCount(postId);
        return Result.success();
    }

    /**
     * 点赞帖子
     * @param postId
     * @return
     */
    @PutMapping("/like/{postId}")
    public Result<String> like(@PathVariable Integer postId) {
        User currentUser = ThreadLocalUtil.getCurrentUser();
        redisLikeService.saveLiked2Redis(currentUser.getId().toString(),postId.toString());
        return Result.success("点赞成功");
    }

    /**
     * 取消点赞
     * @param postId
     * @return
     */
    @PutMapping("/unlike/{postId}")
    public Result<String> unLike(@PathVariable Integer postId) {
        User currentUser = ThreadLocalUtil.getCurrentUser();
        redisLikeService.unlikeFromRedis(currentUser.getId().toString(),postId.toString());
        return Result.success("取消点赞成功");
    }

    /**
     * 获得收藏的帖子
     * @return
     */
    @GetMapping("/collect")
    public Result<List<PostVO>> collectedPosts() {
        User currentUser = ThreadLocalUtil.getCurrentUser();
        List<PostVO> collectedPosts = postService.getCollectedPosts(currentUser);
        return Result.success(collectedPosts);
    }
}
