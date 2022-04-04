package org.csp.rpc.example.spring.provider;


import org.csp.rpc.bootstrap.config.spring.configuration.EnableRpc;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class ProviderMain {

    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(Bootstrap.class);
        context.refresh();
        System.in.read();
    }

    @EnableRpc
    static class Bootstrap {
    }
}
