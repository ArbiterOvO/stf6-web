package com.arbiter.service.service.impl;

import com.arbiter.service.mapper.TagMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.arbiter.service.pojo.po.Tag;
import com.arbiter.service.service.TagService;
import org.springframework.stereotype.Service;

/**
* @author Arbiter
* @description 针对表【tag】的数据库操作Service实现
* @createDate 2025-03-25 20:19:36
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




