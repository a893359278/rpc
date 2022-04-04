package org.csp.rpc.remoting.api;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.csp.rpc.remoting.netty.NettyClientConfig;
import org.csp.rpc.remoting.netty.NettyRemotingClient;

public class NettyExchangeClient implements ExchangeClient {

    private Channel channel;

    public NettyExchangeClient(String host, int port) {
        NettyClientConfig config = new NettyClientConfig();
        config.setIp(host);
        config.setPort(port);
        NettyRemotingClient client = new NettyRemotingClient(config);
        client.connect();
        channel = client.getChannel();
    }

    @Override
    public Object request(Object param) throws Exception {
        ChannelFuture future = channel.writeAndFlush(param);
        return future.get();
    }
}
