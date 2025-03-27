package com.arbiter.service.service.impl;

import com.arbiter.service.mapper.PostLikeMapper;
import com.arbiter.service.mapper.PostMapper;
import com.arbiter.service.pojo.dto.LikedCountDTO;
import com.arbiter.service.pojo.po.Post;
import com.arbiter.service.pojo.po.PostLike;
import com.arbiter.service.service.LikeService;
import com.arbiter.service.service.RedisLikeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final PostLikeMapper postLikeMapper;
    private final RedisLikeService redisLikeService;
    private final PostMapper postMapper;

    @Override
    public PostLike save(PostLike postLike) {
        postLikeMapper.insert(postLike);
        return postLike;
    }

    @Override
    public List<PostLike> saveAll(List<PostLike> list) {
        postLikeMapper.insert(list);
        return list;
    }

    @Override
    public PostLike getByLikedUserIdAndLikedPostId(Integer userId, Integer postId) {
        LambdaQueryWrapper<PostLike> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(PostLike::getPostId,postId);
        queryWrapper.eq(PostLike::getUserId,userId);
        PostLike postLike = postLikeMapper.selectOne(queryWrapper);
        return postLike;
    }

    @Override
    @Transactional
    public void transLikedFromRedis2DB() {
        List<PostLike> list = redisLikeService.getLikedDataFromRedis();
        for (PostLike like : list) {
            PostLike postLike = getByLikedUserIdAndLikedPostId(like.getUserId(), like.getPostId());
            if (postLike == null){
                //没有记录，直接存入
                save(like);
            }else{
                //有记录，需要更新
                postLike.setLikedStatus(like.getLikedStatus());
                postLikeMapper.update(postLike,new LambdaUpdateWrapper<PostLike>().eq(PostLike::getPostId,like.getPostId()));
            }
        }
    }

    @Override
    @Transactional
    public void transLikedCountFromRedis2DB() {
        List<LikedCountDTO> list = redisLikeService.getLikedCountFromRedis();
        for (LikedCountDTO dto : list) {
            Post post = postMapper.selectById(dto.getPostId());
            //点赞数量属于无关紧要的操作，出错无需抛异常
            if (post != null){
                Integer likeNum = post.getLikeNum() + dto.getLikedCount();
                post.setLikeNum(likeNum);
                System.out.println(post.toString());
                //更新点赞数量
                postMapper.updateById(post);
            }
        }
    }
}
