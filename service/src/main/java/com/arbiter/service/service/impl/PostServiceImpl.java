package com.arbiter.service.service.impl;

import com.arbiter.common.constant.RedisConstant;
import com.arbiter.common.enums.LikedStatusEnum;
import com.arbiter.common.po.User;
import com.arbiter.service.mapper.*;
import com.arbiter.service.pojo.dto.PageSearchDTO;
import com.arbiter.service.pojo.dto.PostDTO;
import com.arbiter.service.pojo.po.*;
import com.arbiter.service.pojo.vo.CommentVO;
import com.arbiter.service.pojo.vo.PostDetailVO;
import com.arbiter.service.pojo.vo.PostVO;
import com.arbiter.service.repository.RedisViewRepository;
import com.arbiter.service.service.LikeService;
import com.arbiter.service.service.PostService;
import com.arbiter.service.service.RedisLikeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    private final UserMapper userMapper;
    private final PostMapper postMapper;
    private final PostImgMapper postImgMapper;
    private final PostTagMapper postTagMapper;
    private final TagMapper tagMapper;
    private final CommentMapper commentMapper;
    private final RedisLikeService redisLikeService;
    private final LikeService likeService;
    private final RedisViewRepository reviewRepository;

    public boolean addPost(PostDTO postDTO) {
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setAuthorId(postDTO.getAuthorId());
        postMapper.insert(post);
        for (String img : postDTO.getImgs()) {
            postImgMapper.insert(new PostImg(post.getId(), img));
        }
        for (Integer tagId : postDTO.getTagIds()) {
            postTagMapper.insert(new PostTag(post.getId(),tagId));
        }
        return true;
    }

    public PostDetailVO getPostDetailById(Integer postId) {
        PostDetailVO postDetailVO = new PostDetailVO();
        Post post = postMapper.selectById(postId);
        postDetailVO.setPostId(post.getId());
        postDetailVO.setTitle(post.getTitle());
        postDetailVO.setContent(post.getContent());
        postDetailVO.setAuthorId(post.getAuthorId());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("post_id", postId);
        queryWrapper.orderByDesc("create_time");
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVO> commentVOList = new ArrayList<>();
        for (Comment comment : comments) {
            CommentVO commentVO = new CommentVO();
            commentVO.setContent(comment.getContent());
            commentVO.setPostId(comment.getPostId());
            commentVO.setCreateTime(comment.getCreateTime());
            commentVO.setUserId(comment.getUserId());
            User user = userMapper.selectById(comment.getUserId());
            commentVO.setUserName(user.getUsername());
            commentVO.setUserAvatar(user.getHeadImg());
            commentVOList.add(commentVO);
        }
        postDetailVO.setComments(commentVOList);
        List<PostImg> postImgs = postImgMapper.selectList(new LambdaQueryWrapper<PostImg>().eq(PostImg::getPostId, postId));
        List<String> imgs = new ArrayList<>();
        for (PostImg postImg : postImgs) {
            imgs.add(postImg.getImg());
        }
        postDetailVO.setImgs(imgs);
        List<PostTag> postTags = postTagMapper.selectList(new LambdaQueryWrapper<PostTag>().eq(PostTag::getPostId, postId));
        List<String> tags = new ArrayList<>();
        for (PostTag postTag : postTags) {
            Tag tag = tagMapper.selectById(postTag.getTagId());
            tags.add(tag.getName());
        }
        User user = userMapper.selectById(post.getAuthorId());
        if(!redisLikeService.checkIfLiked(user.getId().toString(),postId.toString())&&likeService.getByLikedUserIdAndLikedPostId(user.getId(),postId)==null)
            postDetailVO.setIsLiked(LikedStatusEnum.UNLIKE.getCode());
        else
            postDetailVO.setIsLiked(LikedStatusEnum.LIKE.getCode());
        postDetailVO.setAuthorName(user.getUsername());
        postDetailVO.setAuthorAvatar(user.getHeadImg());
        postDetailVO.setTags(tags);
        postDetailVO.setCommentCount(comments.size());
        postDetailVO.setLikeCount(post.getLikeNum()+redisLikeService.getLikedCount(post.getId()));
        postDetailVO.setViewCount(post.getViewNum()+reviewRepository.getViewCountById(post.getId()));
        postDetailVO.setUpdateTime(post.getUpdateTime());
        return postDetailVO;
    }

    public List<PostVO> searchPage(PageSearchDTO pageSearchDTO) {
        List<PostVO> postVOList = new ArrayList<>();
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Post::getTitle,pageSearchDTO.getSearchText());
        Page<Post> postPage = postMapper.selectPage(new Page<Post>(pageSearchDTO.getCurrentPage(), pageSearchDTO.getPageSize()), queryWrapper);
        List<Post> posts = postPage.getRecords();
        for (Post post : posts) {
            PostVO postVO = new PostVO();
            postVO.setPostId(post.getId());
            postVO.setTitle(post.getTitle());
            postVO.setContent(post.getContent().length()>30?post.getContent().substring(0,30):post.getContent());
            postVO.setAuthorId(post.getAuthorId());
            User user = userMapper.selectById(post.getAuthorId());
            postVO.setAuthorName(user.getUsername());
            postVO.setAuthorAvatar(user.getHeadImg());
            List<PostTag> postTags = postTagMapper.selectList(new LambdaQueryWrapper<PostTag>().eq(PostTag::getPostId, post.getId()));
            List<String> tags = new ArrayList<>();
            for (PostTag postTag : postTags) {
                Tag tag = tagMapper.selectById(postTag.getTagId());
                tags.add(tag.getName());
            }
            postVO.setTags(tags);
            Long comments = commentMapper.selectCount(new LambdaQueryWrapper<Comment>().eq(Comment::getPostId, post.getId()));
            postVO.setCommentCount(comments.intValue());
            postVO.setLikeCount(post.getLikeNum()+redisLikeService.getLikedCount(post.getId()));
            postVO.setViewCount(post.getViewNum()+reviewRepository.getViewCountById(post.getId()));
            postVO.setUpdateTime(post.getUpdateTime());
            postVOList.add(postVO);
        }

        return postVOList;
    }



}
