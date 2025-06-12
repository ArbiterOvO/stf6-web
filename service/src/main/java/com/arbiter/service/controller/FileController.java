package com.arbiter.service.controller;

import com.arbiter.common.result.Result;
import com.arbiter.common.util.FileUtils;
import com.arbiter.service.configuration.UploadConfig;
import com.arbiter.service.exception.FileUploadException;
import com.arbiter.service.pojo.dto.UploadImageDTO;
import com.arbiter.service.service.impl.FileService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("/file")
public class FileController {
    @Resource
    FileService fileService;

    /*上传图片*/
    @PostMapping("/upload/imgs")
    public Result<List<String>> uploadImages(@RequestParam("file") MultipartFile[] files) throws FileUploadException, InterruptedException {
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = FileUtils.generateFileName();
            fileName=fileName+"."+getExtName(file.getOriginalFilename());
            try {
                fileService.upload(file, fileName);
            }
            catch (IOException e) {
                throw new FileUploadException("文件上传失败");
            }
            String filePath = "/" + fileName;
            String url = UploadConfig.reUrl + filePath;
            System.out.println("返回地址:"+url);
            imageUrls.add(url);
            //一次性上传多张的时候最好加上这个
            Thread.sleep(200);
        }
        return Result.success(imageUrls);
    }

    //获取文件后缀
    private String getExtName(String fileName) {
        String fullName = fileName.toLowerCase();
        int dotIndex = fullName.lastIndexOf(".");
        return dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
    }
}
