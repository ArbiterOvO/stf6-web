package com.arbiter.service.controller;

import com.arbiter.common.result.Result;
import com.arbiter.service.pojo.po.Tag;
import com.arbiter.service.pojo.vo.ForumTagsVO;
import com.arbiter.service.service.TagService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/tag")
public class TagController {

    @Resource
    TagService tagService;

    @GetMapping("/list")
    public Result<List<Tag>> allTagList(){
        List<Tag> list = tagService.list();
        return Result.success(list);
    }

    @GetMapping("/forum")
    public Result<ForumTagsVO> allForumTagList(){
        ForumTagsVO allForumTagList = tagService.getAllForumTagList();
        return Result.success(allForumTagList);
    }
}
