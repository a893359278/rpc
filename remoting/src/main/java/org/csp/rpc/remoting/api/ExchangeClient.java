package org.csp.rpc.remoting.api;

public interface ExchangeClient {
    Object request(Object param) throws Exception;
}
