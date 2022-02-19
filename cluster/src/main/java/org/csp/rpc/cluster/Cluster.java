package org.csp.rpc.cluster;

import org.csp.rpc.registry.api.NodeMetaInfo;

import java.util.Set;

public class Cluster {

    private Directory directory;

    private LoadBalance loadBalance;

    private Route route;

    private Set<NodeMetaInfo> nodeMetaInfos;

    public Cluster(Directory directory, LoadBalance loadBalance, DefaultRoute route) {
        this.directory = directory;
        this.loadBalance = loadBalance;
        this.route = route;
    }

}
