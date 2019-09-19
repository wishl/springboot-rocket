package com.izuche.rocket.configuration;

import com.izuche.rocket.processor.RocketMqComsumerBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RocketProcessorConfig {

    @Bean
    public RocketMqComsumerBeanPostProcessor beanPostProcessor(){
        return new RocketMqComsumerBeanPostProcessor();
    }

}
