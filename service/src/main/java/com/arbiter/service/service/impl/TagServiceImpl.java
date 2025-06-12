package com.arbiter.service.service.impl;

import com.arbiter.common.enums.TagType;
import com.arbiter.service.mapper.TagMapper;
import com.arbiter.service.pojo.vo.ForumTagsVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.arbiter.service.pojo.po.Tag;
import com.arbiter.service.service.TagService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author Arbiter
* @description 针对表【tag】的数据库操作Service实现
* @createDate 2025-03-25 20:19:36
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

    @Override
    public ForumTagsVO getAllForumTagList() {
        ForumTagsVO vo = new ForumTagsVO();
        //热门标签
        List<Tag> hotList = list(new LambdaQueryWrapper<Tag>().eq(Tag::getTagType, TagType.HOT.getCode()));
        //角色标签
        List<Tag> roleList = list(new LambdaQueryWrapper<Tag>().eq(Tag::getTagType, TagType.ROLE.getCode()));
        vo.setHotTags(hotList);
        vo.setRoleTags(roleList);
        return vo;
    }

}




