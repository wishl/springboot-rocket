package com.izuche.rocket.utils;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class RocketSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{"com.izuche.rocket.processor.RocketMqComsumerBeanPostProcessor"};
    }

}
