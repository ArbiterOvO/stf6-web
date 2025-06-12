package com.arbiter.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.arbiter.common.constant.RedisConstant;
import com.arbiter.common.enums.LikedStatusEnum;
import com.arbiter.common.po.User;
import com.arbiter.common.util.RedisLikeUtil;
import com.arbiter.common.util.ThreadLocalUtil;
import com.arbiter.service.mapper.*;
import com.arbiter.service.pojo.dto.PageSearchDTO;
import com.arbiter.service.pojo.dto.PostDTO;
import com.arbiter.service.pojo.po.*;
import com.arbiter.service.pojo.vo.Comment2VO;
import com.arbiter.service.pojo.vo.CommentVO;
import com.arbiter.service.pojo.vo.PostDetailVO;
import com.arbiter.service.pojo.vo.PostVO;
import com.arbiter.service.repository.RedisViewRepository;
import com.arbiter.service.service.CommentService;
import com.arbiter.service.service.LikeService;
import com.arbiter.service.service.PostService;
import com.arbiter.service.service.RedisLikeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
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
    private final CommentService commentService;
    private final Comment2Mapper comment2Mapper;
    private final PostLikeMapper postLikeMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 添加帖子
     * @param postDTO
     * @return
     */
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

    /**
     * 根据id获得帖子详情
     * @param postId
     * @return
     */
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
        //build CommentVO
        List<CommentVO> commentVOList = new ArrayList<>();
        for (Comment comment : comments) {
            User user = userMapper.selectById(comment.getUserId());
            CommentVO commentVO = buildCommentVO(comment, user);
            commentVOList.add(commentVO);
        }
        postDetailVO.setComments(commentVOList);
        //build Imgs
        List<PostImg> postImgs = postImgMapper.selectList(new LambdaQueryWrapper<PostImg>().eq(PostImg::getPostId, postId));
        List<String> imgs = new ArrayList<>();
        for (PostImg postImg : postImgs) {
            imgs.add(postImg.getImg());
        }
        postDetailVO.setImgs(imgs);
        //build tags
        List<PostTag> postTags = postTagMapper.selectList(new LambdaQueryWrapper<PostTag>().eq(PostTag::getPostId, postId));
        List<String> tags = new ArrayList<>();
        for (PostTag postTag : postTags) {
            Tag tag = tagMapper.selectById(postTag.getTagId());
            tags.add(tag.getName());
        }
        //当前登录用户
        User user = ThreadLocalUtil.getCurrentUser();
        //是否点赞
        if(checkIsLiked(user,post))
            postDetailVO.setIsLiked(LikedStatusEnum.LIKE.getCode());
        else
            postDetailVO.setIsLiked(LikedStatusEnum.UNLIKE.getCode());
        postDetailVO.setAuthorName(user.getUsername());
        postDetailVO.setAuthorAvatar(user.getHeadImg());
        postDetailVO.setTags(tags);
        postDetailVO.setCommentCount(comments.size());
        postDetailVO.setLikeCount(post.getLikeNum()+redisLikeService.getLikedCount(post.getId()));
        postDetailVO.setViewCount(post.getViewNum()+reviewRepository.getViewCountById(post.getId()));
        postDetailVO.setUpdateTime(post.getUpdateTime());
        return postDetailVO;
    }

    /**
     * 构建一级评论
     * @param comment
     * @param user
     * @return
     */
    CommentVO buildCommentVO(Comment comment,User user) {
        CommentVO commentVO = new CommentVO();
        BeanUtil.copyProperties(comment,commentVO);
        commentVO.setUserName(user.getUsername());
        commentVO.setUserAvatar(user.getHeadImg());
        commentVO.setLikedNum(comment.getLikeNum());
        commentVO.setHasLiked(commentService.checkLikeComment(comment.getId(),user.getId())?LikedStatusEnum.LIKE.getCode():LikedStatusEnum.UNLIKE.getCode());
        List<Comment2> comment2List = comment2Mapper.selectList(new LambdaQueryWrapper<Comment2>().eq(Comment2::getCommentId, comment.getId()));
        //构建comment2VOList
        List<Comment2VO> comment2VOList = new ArrayList<>();
        for (Comment2 comment2 : comment2List) {
            Comment2VO comment2VO = buildCommentVO2(comment2, user);
            comment2VOList.add(comment2VO);
        }
        commentVO.setComment2VoList(comment2VOList);
        return commentVO;
    }

    /**
     * 构建二级评论
     * @param comment2
     * @param user
     * @return
     */
    Comment2VO buildCommentVO2(Comment2 comment2,User user) {
        Comment2VO comment2VO = new Comment2VO();
        BeanUtil.copyProperties(comment2,comment2VO);
        comment2VO.setUserName(user.getUsername());
        comment2VO.setUserAvatar(user.getHeadImg());
        comment2VO.setLikedNum(comment2.getLikeNum());
        comment2VO.setHasLiked(commentService.checkLikeComment(comment2.getId(),user.getId())?LikedStatusEnum.LIKE.getCode():LikedStatusEnum.UNLIKE.getCode());
        return comment2VO;
    }

    /**
     * 帖子搜索
     * @param pageSearchDTO
     * @return
     */
    public List<PostVO> searchPage(PageSearchDTO pageSearchDTO) {
        List<PostVO> postVOList = new ArrayList<>();
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Post::getTitle,pageSearchDTO.getSearchText());
        //判断显示审核过的还是未审核的
        if (pageSearchDTO.getHasChecked()!=null){
            queryWrapper.eq(Post::getIsChecked,pageSearchDTO.getHasChecked());
        }
        //标签
        if (!pageSearchDTO.getTags().isEmpty()){
            List<Integer> tagIds=new ArrayList<>();
            for (Tag tag : pageSearchDTO.getTags()) {
                tagIds.add(tag.getId());
            }
            List<PostTag> postTags = postTagMapper.selectList(new LambdaQueryWrapper<PostTag>().in(PostTag::getTagId, tagIds));
            List<Integer> postIds=new ArrayList<>();
            for (PostTag postTag : postTags) {
                postIds.add(postTag.getPostId());
            }
            if (!postIds.isEmpty()){
                queryWrapper.in(Post::getId,postIds);
            }
            else {
                return null;
            }
        }
        //最新的在前
        queryWrapper.orderByDesc(Post::getCreateTime);

        //搜索帖子
        Page<Post> postPage = postMapper.selectPage(new Page<Post>(pageSearchDTO.getCurrentPage(), pageSearchDTO.getPageSize()), queryWrapper);
        List<Post> posts = postPage.getRecords();
        //封装postVOList
        for (Post post : posts) {
            PostVO postVO = buildPostVO(post);
            postVOList.add(postVO);
        }

        return postVOList;
    }

    /**
     * 获取收藏的帖子
     * @param user
     * @return
     */
    @Override
    public List<PostVO> getCollectedPosts(User user) {
        Integer id = user.getId();
        List<PostLike> postLikes = postLikeMapper.selectList(new LambdaQueryWrapper<PostLike>().eq(PostLike::getUserId, id).eq(PostLike::getLikedStatus,LikedStatusEnum.LIKE.getCode()));
        List<Integer> postIds = new ArrayList<>();
        for (PostLike postLike : postLikes) {
            Integer postId = postLike.getPostId();
            postIds.add(postId);
        }
        //redis中的ids
        List<Integer> likedPostId = redisLikeService.getLikedPostId(id);
        postIds.addAll(likedPostId);
        if (postIds.isEmpty()){
            return null;
        }
        List<Post> posts = postMapper.selectBatchIds(postIds);
        Iterator<Post> iterator = posts.iterator();
        while (iterator.hasNext()) {
            Post post = iterator.next();
            if (!checkIsLiked(user,post)){
                posts.remove(post);
            }
        }

        List<PostVO> postVOList = new ArrayList<>();
        for (Post post : posts) {
            PostVO postVO = buildPostVO(post);
            postVOList.add(postVO);
        }
        return postVOList;
    }

    @Override
    public void updatePost(PostDTO postDTO) {
        //更新标题和内容
        Post post = new Post();
        BeanUtil.copyProperties(postDTO,post);
        post.setId(postDTO.getPostId());
        log.info("post:{}", post.toString());
        updateById(post);
        //删除原有tag关系 增加新关系
        postTagMapper.delete(new LambdaQueryWrapper<PostTag>().eq(PostTag::getPostId,post.getId()));
        List<PostTag> postTagList=new ArrayList<>();
        for (Integer tagId : postDTO.getTagIds()) {
            PostTag postTag = new PostTag();
            postTag.setPostId(post.getId());
            postTag.setTagId(tagId);
            postTagList.add(postTag);
        }
        postTagMapper.insert(postTagList);
        //删除原有图片关系 增加新关系
        postImgMapper.delete(new LambdaQueryWrapper<PostImg>().eq(PostImg::getPostId,post.getId()));
        List<PostImg> postImgList=new ArrayList<>();
        for (String img : postDTO.getImgs()) {
            PostImg postImg = new PostImg();
            postImg.setPostId(post.getId());
            postImg.setImg(img);
            postImgList.add(postImg);
        }
        postImgMapper.insert(postImgList);

    }

    /**
     * 构建PostVO
     * @param post
     * @return
     */
    PostVO buildPostVO(Post post) {
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
        return postVO;
    }

    /**
     * 判断是否已经点赞
     * @param user
     * @param post
     * @return
     */
    boolean checkIsLiked(User user,Post post) {
        Integer userId = user.getId();
        Integer postId = post.getId();
        String key = RedisLikeUtil.getLikedKey(userId.toString(), postId.toString());
        Integer o = (Integer) redisTemplate.opsForHash().get(RedisLikeUtil.MAP_KEY_USER_LIKED, key);
        if (o==null)
        {
            if (likeService.getByLikedUserIdAndLikedPostId(user.getId(),postId)==null)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            if (o.intValue()==LikedStatusEnum.UNLIKE.getCode())
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }
}
