package com.izuche.rocket.configuration;

import com.izuche.rocket.domain.ConsumerRecord;
import com.izuche.rocket.domain.ListenerHolder;
import com.izuche.rocket.domain.RocketMqSession;
import com.izuche.rocket.properties.RocketMQComsumerProperties;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration("rocketMQConfig")
@ConditionalOnClass(RocketMQComsumerProperties.class)
@EnableConfigurationProperties(RocketMQComsumerProperties.class)
public class RocketMQConfig implements BeanFactoryAware {

    @Autowired
    private RocketMQComsumerProperties rocketMQComsumerProperties;

    private static final Logger logger = LoggerFactory.getLogger(RocketMQConfig.class);

    private BeanFactory beanFactory;

    public boolean createListener(RocketMqSession session){
        int maxThread = rocketMQComsumerProperties.getMaxThread();
        int minThread = rocketMQComsumerProperties.getMinThread();
        // 同一项目启动不同监听时，同一group会报错
//        String groupName = rocketMQComsumerProperties.getGroupName();
        String namesrvAddr = rocketMQComsumerProperties.getNamesrvAddr();
        String group = session.getGroup();
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(group);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(minThread);
        consumer.setConsumeThreadMax(maxThread);
        try {
            createListener(consumer,session);
            logger.info("创建mq监听,topic:{},group:{}",session.getTopic(),group);
        } catch (Exception e) {
            logger.error("创建mq监听失败,topic:{},message:{}",session.getTopic(), Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    private void createListener(DefaultMQPushConsumer consumer,RocketMqSession session) throws Exception {
        Method method = session.getMethod();
        // 注册
        consumer.subscribe(session.getTopic(),session.getTag());
        Class<?> returnType = method.getReturnType();
        if(!returnType.isAssignableFrom(ConsumeConcurrentlyStatus.class)){
            throw new Exception("返回值不正确,必须是ConsumeConcurrentlyStatus类型");
        }
        Class<?>[] parameterTypes = method.getParameterTypes();// 方法参数
        if(parameterTypes.length!=1||!parameterTypes[0].isAssignableFrom(ConsumerRecord.class)){
            throw new Exception("参数必须只有一个,并且类型为ConsumerRecord");
        }
        MessageListenerConcurrently message = (List<MessageExt> msgs, ConsumeConcurrentlyContext context) -> {
            List<MessageExt> param = new ArrayList<>();
            for (MessageExt msg : msgs) {
                if(session.isIgnore(msg.getTags())){
                    logger.info("当前topic:{},tags:{}被设置忽略",msg.getTopic(),msg.getTags());
                    continue;
                }
                logger.debug("当前topic:{}被消费data:{}",msg.getTopic(),msg.getBody());
                param.add(msg);
            }
            ConsumerRecord record = new ConsumerRecord();
            record.setContext(context);
            record.setMsgs(param);
            try {
                return (ConsumeConcurrentlyStatus) method.invoke(session.getObject(),record);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                logger.error("执行出错,请确认方法被public修饰");
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                logger.error("执行出错");
            }
            // 等会儿消费
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        };
        // 添加监听方法
        consumer.registerMessageListener(message);
        // 启动监听
        consumer.start();
        // 存储监听满足监控需求
        ListenerHolder.cache(message,session.getTopic());
    }

}
