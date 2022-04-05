package org.csp.rpc.remoting.api;

import org.csp.rpc.core.extension.SPI;

@SPI("all")
public interface ChannelHandler {
    void read(Object msg);
}
