package com.arbiter.service.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mail.from")
@Data
public class EmailProperties {
    /**
     * 发送者地址
     */
    private String addr;
}
