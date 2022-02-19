package org.csp.rpc.cluster;

import org.csp.rpc.bootstrap.Invoker;
import org.csp.rpc.registry.api.NodeMetaInfo;

import java.util.List;
import java.util.Set;

public class DefaultDirectory implements Directory {

    private String name;

    private Set<NodeMetaInfo> metaInfos;

    List<Invoker> invoker;

    public DefaultDirectory(Set<NodeMetaInfo> list) {
        this.metaInfos = list;
        this.name = ((NodeMetaInfo) metaInfos.toArray()[0]).getServiceName();
        this.invoker = convertToInvokers(list);
    }


    @Override
    public List<Invoker> list(String name) {
        return invoker;
    }

    @Override
    public List<Invoker> convertToInvokers(Set<NodeMetaInfo> info) {

        return null;
    }

}
