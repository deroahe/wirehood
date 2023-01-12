package com.wirehood.adminservice.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "admin")
public class StartupProperties {

    private String username;

    private String fullName;

    private String password;

    private Character status;

    private String emailAddress;

    private Integer loginAttempt;

    private Long profileId;
}
