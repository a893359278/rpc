package org.csp.rpc.registry.api;

import java.util.Map;

public class NodeMetaInfo {
    private String ip;

    private Integer port;

    private String serviceName;

    private Map<String, String> payload;

    public NodeMetaInfo() {
    }

    public Map<String, String> getMetaData() {
        return payload;
    }

    public String getId() {
        return ip + ":" + port;
    }


    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Map<String, String> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, String> payload) {
        this.payload = payload;
    }
}
