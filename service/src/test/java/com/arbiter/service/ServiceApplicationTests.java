package com.arbiter.service;

import com.arbiter.common.po.User;
import com.arbiter.service.mapper.PostImgMapper;
import com.arbiter.service.mapper.PostTagMapper;
import com.arbiter.service.pojo.po.Comment;
import com.arbiter.service.pojo.po.Post;
import com.arbiter.service.pojo.po.PostImg;
import com.arbiter.service.pojo.po.PostTag;
import com.arbiter.service.service.CommentService;
import com.arbiter.service.service.PostService;
import com.arbiter.service.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class ServiceApplicationTests {

    @Autowired
    private UserService userService;
    @Test
    void contextLoads() {
        User user = new User();
        user.setUsername("张三");
        user.setPassword("123456");
        user.setEmail("321@qq.com");
        userService.save(user);
    }

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostImgMapper postImgMapper;
    @Autowired
    private PostTagMapper postTagMapper;
    @Test
    void postTest()
    {
        Post post = new Post();
        post.setAuthorId(1);
        post.setTitle("测试1");
        post.setContent("测试内容");
        postService.save(post);
        Comment comment=new Comment();
        comment.setUserId(1);
        comment.setPostId(post.getId());
        comment.setContent("测试回复1");
        commentService.save(comment);
        postImgMapper.insert(new PostImg(post.getId(),"https://th.bing.com/th/id/OIP.c2qhgMnUyx40egZk3EyBjQHaEK?w=308&h=180&c=7&r=0&o=5&pid=1.7"));
        postTagMapper.insert(new PostTag(post.getId(),1));
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void redisTest()
    {
        redisTemplate.opsForValue().set("a","1");
        String a = (String)redisTemplate.opsForValue().get("a");
        System.out.println(a);
    }
}
