package org.csp.rpc.bootstrap.config.spring.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RpcConfigurationRegistrar.class)
public @interface RpcConfiguration {
}
