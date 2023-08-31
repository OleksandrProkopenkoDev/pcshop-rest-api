package com.spro.pcshop.configs;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

import java.net.http.HttpClient;

@Configuration
public class AppConfig {
    @Bean
    public WebClient webClient() {
        // Increase the buffer size to a larger value (e.g., 1 MB)
        int bufferSizeInBytes = 1024 * 1024; // 1 MB

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(bufferSizeInBytes))
                .build();

        return WebClient.builder()
                .exchangeStrategies(strategies)
                .build();
    }
}
