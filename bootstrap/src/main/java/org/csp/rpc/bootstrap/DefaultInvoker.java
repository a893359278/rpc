package org.csp.rpc.bootstrap;


import org.csp.rpc.remoting.api.ExchangeClient;
import org.csp.rpc.remoting.api.NettyExchangeClient;
import org.csp.rpc.remoting.handler.RpcInvokerParam;

public class DefaultInvoker<T> implements Invoker<T> {

    private Class<T> proxyInterface;

    private Node node;

    private ExchangeClient exchangeClient;

    public DefaultInvoker(Class<T> proxyInterface, Node node) {
        this.proxyInterface = proxyInterface;
        this.node = node;
        exchangeClient = new NettyExchangeClient(node.getHost(), node.getPort());
    }

    @Override
    public Class<T> getInterface() {
        return proxyInterface;
    }

    @Override
    public Object invoker(RpcInvokerParam param) throws Exception {
        return exchangeClient.request(param);
    }

    @Override
    public Node getNode() {
        return node;
    }

}
