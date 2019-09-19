package com.izuche.rocket.annontation;

import com.izuche.rocket.configuration.RocketProcessorConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Import(RocketProcessorConfig.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RocketListener {

    String group();
    String topic();
    String tag() default "*";
    String ignoreTags() default "";

}
