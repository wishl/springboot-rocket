package com.izuche.rocket.utils;

import java.util.HashMap;
import java.util.Map;

public class CollectionsUtil {

    public static Map<String,Object> arrayToMap(String[] array){
        Map<String,Object> resultMap = new HashMap<>();
        if(array==null||array.length==0){
            return resultMap;
        }
        for (String s : array) {
            resultMap.put(s,s);
        }
        return resultMap;
    }


}
