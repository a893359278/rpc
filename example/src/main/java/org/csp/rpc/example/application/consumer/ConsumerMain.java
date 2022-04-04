package org.csp.rpc.example.application.consumer;

import org.csp.rpc.bootstrap.DefaultInvoker;
import org.csp.rpc.bootstrap.Node;
import org.csp.rpc.bootstrap.ProxyFactory;
import org.csp.rpc.example.api.HelloService;

public class ConsumerMain {
    public static void main(String[] args) {
        Node node = new Node("127.0.0.1", 20011, "rpc");
        DefaultInvoker<HelloService> invoker = new DefaultInvoker<>(HelloService.class, node);
        HelloService proxy = ProxyFactory.getProxy(invoker, HelloService.class);
        proxy.hello("1111");
    }
}
