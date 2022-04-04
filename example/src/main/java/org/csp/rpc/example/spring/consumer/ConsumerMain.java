package org.csp.rpc.example.spring.consumer;

import org.csp.rpc.bootstrap.config.spring.configuration.EnableRpc;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConsumerMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(Bootstrap.class);
        context.refresh();
    }

    @EnableRpc
    static class Bootstrap {
    }
}
