package com.woltappsummer.BackendTest;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("my-properties")
@Data
public class ApplicationProperties {
    private int port;
    private String filename;
}
