package com.izuche.rocket.configuration;

import com.izuche.rocket.domain.ProduceHolder;
import com.izuche.rocket.properties.RocketMqProducterPropreties;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(RocketMqProducterPropreties.class)
@EnableConfigurationProperties(RocketMqProducterPropreties.class)
public class ReocketMQProductConfig {

    @Autowired
    private RocketMqProducterPropreties rocketMqProducterPropreties;

    private static final Logger logger = LoggerFactory.getLogger(ReocketMQProductConfig.class);

    @Bean
    public ProduceHolder produceHolder (){
        ProduceHolder produceHolder = new ProduceHolder();
        try {
            String config = rocketMqProducterPropreties.getProducterConfig();
            if(StringUtils.isNotBlank(config)) {
                String[] split = config.split(",");
                for (String s : split) {
                    String[] addrAndGroup = s.split("@");
                    String name = addrAndGroup[0];
                    String addr = addrAndGroup[1];
                    String group = addrAndGroup[2];
                    logger.info("创建rocketmq生产者,name:{},address:{},group:{}", addr, group);
                    DefaultMQProducer producer = new DefaultMQProducer(group);
                    producer.setNamesrvAddr(addr);
                    producer.start();
                    boolean result = produceHolder.putProducer(name, producer);
                    if (!result) {
                        logger.info("放入produceHolder失败,name:{},address:{},group:{}", name, addr, group);
                    }
                }
            }else{
                logger.info("没有config不生成produce");
            }
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return produceHolder;
    }

}
