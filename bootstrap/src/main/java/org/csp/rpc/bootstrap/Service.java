package org.csp.rpc.bootstrap;


import java.lang.annotation.*;

/**
 * 服务提供者
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {

    int timeout() default 0;

    int retires() default 0;

    int weight() default 0;
}
