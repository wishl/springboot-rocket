package com.izuche.rocket.domain;

import com.izuche.rocket.utils.CollectionsUtil;

import java.lang.reflect.Method;
import java.util.Map;

public class RocketMqSession {

    private String group;
    private String topic;
    private String tag;
    private String ignoreTags;
    private Map<String,Object> tags;
    private Method method;
    private Object object;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTag() {
        return tag;
    }

    public String getTopic() {
        return topic;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setIgnoreTags(String ignoreTags) {
        this.ignoreTags = ignoreTags;
        String[] split = ignoreTags.split(",");
        this.tags = CollectionsUtil.arrayToMap(split);
    }

    public boolean isTopic(String topic){
        return topic.equals(this.topic);
    }

    public boolean isTags(String tag){
        if(tag.equals("*"))
            return true;
        return tag.equals(this.tag);
    }

    public boolean isIgnore(String tag){
        return tags.containsKey(tag);
    }

}
