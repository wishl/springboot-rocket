package com.izuche.rocket.processor;

import com.izuche.rocket.annontation.RocketListener;
import com.izuche.rocket.configuration.RocketMQConfig;
import com.izuche.rocket.domain.RocketMqSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;

// 先执行setBeanFactory，然后执行postProcessBeforeInitialization
public class RocketMqComsumerBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware, EnvironmentAware {

    private Environment environment;

    private static final Logger logger = LoggerFactory.getLogger(RocketMqComsumerBeanPostProcessor.class);

    private BeanFactory beanFactory;

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        getAnnontation(bean,beanName);
        return bean;
    }

    // 获取注解，并创建监听
    private void getAnnontation(Object bean,String beanName){
        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            RocketListener annotation = method.getAnnotation(RocketListener.class);
            if(annotation!=null){
                String topic = annotation.topic();
                String group = getGroupForEnvroiment(annotation);
                String tag = annotation.tag();
                String ignoreTags = annotation.ignoreTags();
                RocketMqSession session = new RocketMqSession();
                session.setIgnoreTags(ignoreTags);
                session.setTag(tag);
                session.setTopic(topic);
                session.setMethod(method);
                session.setObject(bean);
                session.setGroup(group);
                RocketMQConfig config = (RocketMQConfig) beanFactory.getBean("rocketMQConfig");
                // 创建监听者
                boolean result = config.createListener(session);
                if(result){
                    logger.info("创建mq成功，beanName:{},method:{}",beanName,method.getName());
                }
            }
        }
    }

    private String getGroupForEnvroiment(RocketListener rocketListener){
        String group = environment.getProperty(rocketListener.group());
        if(StringUtils.isNotBlank(group)){
            return group;
        }
        logger.info("无法从配置文件中获取group,使用注解中的group,group={}",rocketListener.group());
        return rocketListener.group();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
