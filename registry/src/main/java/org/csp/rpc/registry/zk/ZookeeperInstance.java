package org.csp.rpc.registry.zk;

import java.util.Map;

public class ZookeeperInstance {

    public ZookeeperInstance() {
    }

    public ZookeeperInstance(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    private Map<String, String> metadata;

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
