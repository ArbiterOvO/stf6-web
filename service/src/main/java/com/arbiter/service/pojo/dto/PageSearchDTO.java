package com.arbiter.service.pojo.dto;

import com.arbiter.service.pojo.po.Tag;
import lombok.Data;

import java.util.List;

@Data
public class PageSearchDTO {
    /**
     * 分页参数
     */
    private Integer currentPage;
    private Integer pageSize;
    private String searchText;

    //是否已经审核 0未审核 1已审核
    private Integer hasChecked;

    //收藏id
    private Integer likeUserId;

    //标签
    private List<Tag> tags;
}
