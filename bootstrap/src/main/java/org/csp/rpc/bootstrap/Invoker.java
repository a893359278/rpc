package org.csp.rpc.bootstrap;


import org.csp.rpc.remoting.handler.RpcInvokerParam;

public interface Invoker<T> {
    /**
     * 获取代理的接口对象
     * @return
     */
    Class<T> getInterface();

    /**
     * 执行 RPC 调用
     * @param param
     * @return
     */
    Object invoker(RpcInvokerParam param) throws Exception;

    /**
     * 远程节点信息
     * @return
     */
    Node getNode();
}
