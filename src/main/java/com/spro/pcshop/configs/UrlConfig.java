package com.spro.pcshop.configs;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class UrlConfig {

    @Value("${application.PATH_URL}")
    private  String PATH_URL;
    @Value("${application.HOST_URL}")
    private  String HOST_URL;
    @Value("${application.PORT}")
    private  String PORT;

}
