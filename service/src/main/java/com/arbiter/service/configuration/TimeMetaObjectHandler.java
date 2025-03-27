package com.arbiter.service.configuration;


import com.arbiter.service.pojo.BaseEntity;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimeMetaObjectHandler implements MetaObjectHandler {

    /**
     * 新增时的操作
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        if (ObjectUtils.isEmpty(metaObject) || !(metaObject.getOriginalObject() instanceof BaseEntity)) {
            return;
        }
        BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();
        LocalDateTime createTime = ObjectUtils.isNotNull(baseEntity.getCreateTime())
                ? baseEntity.getCreateTime() : LocalDateTime.now().withNano(0);
        LocalDateTime updateTime = ObjectUtils.isNotNull(baseEntity.getUpdateTime())
                ? baseEntity.getUpdateTime() : LocalDateTime.now().withNano(0);
        baseEntity.setCreateTime(createTime);
        baseEntity.setUpdateTime(updateTime);
    }

    /**
     * 修改时的操作
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        if (ObjectUtils.isEmpty(metaObject) || !(metaObject.getOriginalObject() instanceof BaseEntity)) {
            return;
        }
        BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();
        LocalDateTime updateTime = LocalDateTime.now().withNano(0);
        baseEntity.setUpdateTime(updateTime);
    }
}
