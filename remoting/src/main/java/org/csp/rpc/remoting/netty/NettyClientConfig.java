package org.csp.rpc.remoting.netty;

public class NettyClientConfig {

    private int connectTimeoutMillis = 3000;

    private long idleTimeoutMillis = 60000;
    private int port = 28880;
    private String ip = "127.0.0.1";

    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public void setIdleTimeoutMillis(long idleTimeoutMillis) {
        this.idleTimeoutMillis = idleTimeoutMillis;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public long getIdleTimeoutMillis() {
        return idleTimeoutMillis;
    }
}
