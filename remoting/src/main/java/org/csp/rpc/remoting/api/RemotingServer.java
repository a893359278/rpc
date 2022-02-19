package org.csp.rpc.remoting.api;

import org.csp.rpc.core.extension.SPI;

@SPI("nettyServer")
public interface RemotingServer {

    /**
     * start remoting server.
     */
    void start();

    /**
     * close remoting server.
     */
    void close();
}
