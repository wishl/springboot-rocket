package com.izuche.rocket.domain;

import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ListenerHolder {

    private static Map<String, MessageListenerConcurrently> cache;

    static {
        cache = new ConcurrentHashMap<>();
    }

    public static void cache(MessageListenerConcurrently listener,String topic){
        cache.put(topic,listener);
    }

    public static MessageListenerConcurrently get(String topic){
        return cache.get(topic);
    }

}
