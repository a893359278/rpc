package org.csp.rpc.bootstrap.config.spring.context;

import org.csp.rpc.bootstrap.RpcBootstrap;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

import java.util.Objects;

public class RpcBootstrapListener implements ApplicationListener, Ordered {

    private RpcBootstrap bootstrap = RpcBootstrap.getInstance();

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (Objects.isNull(event)) {
            return;
        }

        if (event instanceof ContextClosedEvent) {
            bootstrap.close();
        } else if (event instanceof ContextRefreshedEvent) {
            bootstrap.start();
        }
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
