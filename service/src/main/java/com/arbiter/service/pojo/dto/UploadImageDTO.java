package com.arbiter.service.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadImageDTO {
    Integer postId;
    String type;
}
