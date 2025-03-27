package com.arbiter.service.pojo.dto;

import lombok.Data;

@Data
public class PageSearchDTO {
    private Integer currentPage;
    private Integer pageSize;
    private String searchText;
}
