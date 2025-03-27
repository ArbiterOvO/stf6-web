package com.arbiter.service.service;

import com.arbiter.service.pojo.dto.PageSearchDTO;
import com.arbiter.service.pojo.dto.PostDTO;
import com.arbiter.service.pojo.po.Post;
import com.arbiter.service.pojo.vo.PostDetailVO;
import com.arbiter.service.pojo.vo.PostVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface PostService extends IService<Post> {

    boolean addPost(PostDTO postDTO);

    PostDetailVO getPostDetailById(Integer postId);

    List<PostVO> searchPage(PageSearchDTO pageSearchDTO);

}
