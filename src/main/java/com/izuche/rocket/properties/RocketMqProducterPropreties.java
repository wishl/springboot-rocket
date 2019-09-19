package com.izuche.rocket.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.rocket.mq.producer")
public class RocketMqProducterPropreties {

   private String producterConfig;

    public String getProducterConfig() {
        return producterConfig;
    }

    public void setProducterConfig(String producterConfig) {
        this.producterConfig = producterConfig;
    }
}
