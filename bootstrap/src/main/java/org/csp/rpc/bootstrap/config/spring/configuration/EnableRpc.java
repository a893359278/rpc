package org.csp.rpc.bootstrap.config.spring.configuration;

import org.csp.rpc.bootstrap.config.spring.context.RpcComponentScan;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RpcConfiguration
@RpcComponentScan
public @interface EnableRpc {
}
