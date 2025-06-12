package com.arbiter.common.enums;

import lombok.Getter;

@Getter
public enum TagType {
    HOT(0, "热门"),
    ROLE(1, "角色"),
    ;

    private Integer code;

    private String msg;

    TagType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
