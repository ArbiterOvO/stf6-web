package com.arbiter.common.enums;

import lombok.Getter;

@Getter
public enum CheckedStatusEnum{
    notChecked(0,"未审核"),
    hasChecked(1,"已审核"),
    ;

    private Integer code;

    private String msg;

    CheckedStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
