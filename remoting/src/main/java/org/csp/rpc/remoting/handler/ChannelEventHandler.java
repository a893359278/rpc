package org.csp.rpc.remoting.handler;

import org.csp.rpc.core.registry.ServiceRegistry;

import java.lang.reflect.Method;

public class ChannelEventHandler implements Runnable {

    private static final ServiceRegistry registry = ServiceRegistry.getInstance();


    private Object msg;

    public ChannelEventHandler(Object msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        if (msg instanceof RpcInvokerParam) {
            RpcInvokerParam param = (RpcInvokerParam) msg;
            Object service = registry.getService(param.getCls());
            try {
                Method m = service.getClass().getMethod(param.getMethod(), param.getParameters());
                Object invoke = m.invoke(service, param.getArgs());
                System.out.println(invoke);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
