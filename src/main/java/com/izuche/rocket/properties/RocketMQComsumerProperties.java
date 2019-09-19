package com.izuche.rocket.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.rocket.mq.comsumer")
public class RocketMQComsumerProperties {

    private String namesrvAddr;
    private String topic;
    private int maxThread;
    private int minThread;
    private String listener;

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getMaxThread() {
        return maxThread;
    }

    public void setMaxThread(int maxThread) {
        this.maxThread = maxThread;
    }

    public int getMinThread() {
        return minThread;
    }

    public void setMinThread(int minThread) {
        this.minThread = minThread;
    }

    public String getListener() {
        return listener;
    }

    public void setListener(String listener) {
        this.listener = listener;
    }
}
