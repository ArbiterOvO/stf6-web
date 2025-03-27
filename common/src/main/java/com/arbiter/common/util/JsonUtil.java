package com.arbiter.common.util;

import com.alibaba.fastjson2.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    //对象转JSON
    public static JSONObject toJson(Object obj) {
        return JSONObject.parseObject(JSONObject.toJSONString(obj));
    }

    //数组转Json
    public static <T> List<JSONObject>  ListToJsonList(List<T> list) {
        List<JSONObject> jsonList = new ArrayList<>();
        for (T t : list) {
            jsonList.add(toJson(t));
        }
        return jsonList;
    }
}
