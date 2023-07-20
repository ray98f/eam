package com.wzmtr.eam.config.annotation;

import java.lang.annotation.*;

/**
 * @author frp
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogMaker {
    String value() default "";

    String method() default "";

    String url() default "";

    String type() default "";
}
