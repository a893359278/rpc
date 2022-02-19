package org.csp.rpc.bootstrap;

import java.lang.reflect.Proxy;

public class ProxyFactory {

    public static <T> T getProxy(Invoker invoker, Class<?> cls) {
        Class[] classes = cls.isInterface() ? new Class[]{cls} : cls.getInterfaces();
        return (T) Proxy.newProxyInstance(cls.getClassLoader(), classes, new RpcInvocationHandler(invoker));
    }

}
