package com.izuche.rocket.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProductorHolder {

    private Map<String,Object> map = new ConcurrentHashMap<>();


}
