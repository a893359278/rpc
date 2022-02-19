package org.csp.rpc.bootstrap.config.spring.context;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RpcComponentScanRegistrar.class)
public @interface RpcComponentScan {

    /**
     * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation
     * declarations e.g.: {@code @RpcComponentScan("org.my.packages")} instead of
     * {@code @PpcComponentScan(basePackages="org.my.packages")}.
     *
     * @return the base packages scan
     */
    String [] value() default {};

    /**
     *
     * @return the base packages scan
     */
    String [] basePackages() default {};
}
