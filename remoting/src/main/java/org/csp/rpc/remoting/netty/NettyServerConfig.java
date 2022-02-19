package org.csp.rpc.remoting.netty;

import io.netty.channel.epoll.Epoll;

public class NettyServerConfig {

    private boolean useEpoll = false;
    private int ioThread = Runtime.getRuntime().availableProcessors() + 1;
    private boolean keepAlive = false;
    private long idleTimeoutMillis = 3000L;

    private int port = 28880;

    public boolean useEpoll() {
        if (useEpoll) {
            String osName = System.getProperty("os.name");
            return osName.toLowerCase().contains("linux") && Epoll.isAvailable();
        }
        return false;
    }

    public int getWorkThreads() {
        return ioThread;
    }

    public boolean getKeepAlive() {
        return keepAlive;
    }

    public long getIdleTimeoutMillis() {
        return idleTimeoutMillis;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
