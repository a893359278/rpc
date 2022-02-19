package org.csp.rpc.cluster;

import org.csp.rpc.bootstrap.Invoker;
import org.csp.rpc.registry.api.NodeMetaInfo;

import java.util.List;
import java.util.Set;

public interface Directory {
    List<Invoker> list(String name);

    List<Invoker> convertToInvokers(Set<NodeMetaInfo> info);
}
