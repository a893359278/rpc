package org.csp.rpc.registry.api;

import org.csp.rpc.core.extension.SPI;

import java.util.Set;

@SPI("zookeeper")
public interface RegistryCenter {

    void connect(String discoveryAddress);

    void registry(NodeMetaInfo info);

    Set<NodeMetaInfo> list(String name);
}
