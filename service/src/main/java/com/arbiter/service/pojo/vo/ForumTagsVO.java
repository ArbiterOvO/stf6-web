package com.arbiter.service.pojo.vo;

import com.arbiter.service.pojo.po.Tag;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ForumTagsVO {
    /**
     * 热门标签
     */
    private List<Tag> hotTags;
    /**
     * 角色标签
     */
    private List<Tag> roleTags;
}
