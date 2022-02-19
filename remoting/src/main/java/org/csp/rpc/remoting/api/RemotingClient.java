package org.csp.rpc.remoting.api;

import org.csp.rpc.core.extension.SPI;

@SPI("nettyClient")
public interface RemotingClient {

    /**
     * connect remoting server.
     */
    void connect();

    /**
     * close connection.
     */
    void close();
}
