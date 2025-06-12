package com.arbiter.service.service.impl;

import cn.hutool.core.io.FileUtil;
import com.arbiter.common.util.FileUtils;
import com.arbiter.service.configuration.UploadConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class FileService {

    public void upload(MultipartFile file, String fileName) throws IOException {
        //创建目录
        FileUtil.mkdir(UploadConfig.path );
        log.info("目录创建成功或已存在");

//        String newPath = UploadConfig.path  + "/"+fileName;
        String newPath = UploadConfig.path  + "\\"+fileName;
        log.info("准备写入文件到: {}", newPath);
        FileUtils.write(newPath, file.getInputStream());
        log.info("文件写入成功");
    }
}
