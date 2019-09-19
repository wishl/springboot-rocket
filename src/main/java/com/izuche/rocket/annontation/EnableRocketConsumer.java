package com.izuche.rocket.annontation;

import com.izuche.rocket.utils.RocketSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({RocketSelector.class})
public @interface EnableRocketConsumer {
}
