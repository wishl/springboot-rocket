package com.izuche.rocket.domain;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class ConsumerRecord {

    private List<MessageExt> msgs;
    private ConsumeConcurrentlyContext context;

    public List<MessageExt> getMsgs() {
        return msgs;
    }

    public void setMsgs(List<MessageExt> msgs) {
        this.msgs = msgs;
    }

    public ConsumeConcurrentlyContext getContext() {
        return context;
    }

    public void setContext(ConsumeConcurrentlyContext context) {
        this.context = context;
    }
}
