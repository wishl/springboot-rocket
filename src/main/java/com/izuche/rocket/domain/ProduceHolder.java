package com.izuche.rocket.domain;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProduceHolder {

    private Map<String,DefaultMQProducer> map = new ConcurrentHashMap<>();

    public boolean putProducer(String name,DefaultMQProducer mqProducer){
        if(!map.containsKey(name)){
            map.put(name,mqProducer);
            return true;
        }
        return false;
    }

    public boolean send(String name,String topic,String message,String key,String tag){
        try {
            DefaultMQProducer producer = map.get(name);
            if(producer == null){
                return false;
            }
            Message mqMessage = new Message(topic,tag,key,message.getBytes());
            producer.send(mqMessage);
            return true;
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

}
