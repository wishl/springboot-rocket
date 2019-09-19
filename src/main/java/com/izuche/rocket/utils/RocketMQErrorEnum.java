package com.izuche.rocket.utils;

public enum RocketMQErrorEnum {

    PARAMM_NULL(100);

    private int errorCode;

    RocketMQErrorEnum(int errorCode){
        this.errorCode = errorCode;
    }

    public int ErrorCode(){
        return errorCode;
    }



}
