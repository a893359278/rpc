package org.csp.rpc.remoting.netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.csp.rpc.remoting.api.RemotingClient;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class NettyRemotingClient implements RemotingClient {

    private Bootstrap bootstrap;
    private EventLoopGroup eventLoopGroupWork;
    private NettyClientConfig nettyClientConfig;
    private Channel channel;

    public NettyRemotingClient(NettyClientConfig nettyClientConfig) {
        this.nettyClientConfig = nettyClientConfig;
    }

    @Override
    public void connect() {
        this.bootstrap = new Bootstrap();
        eventLoopGroupWork = new NioEventLoopGroup(1, new ThreadFactory() {
            private AtomicInteger threadIndex = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, String.format("NettyClientSelector_%d", this.threadIndex.incrementAndGet()));
            }
        });

        bootstrap.group(eventLoopGroupWork).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, nettyClientConfig.getConnectTimeoutMillis())
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(nettyClientConfig.getIdleTimeoutMillis() ,0, 0, TimeUnit.MILLISECONDS))
                                .addLast(new MessageDecoder())
                                .addLast(new MessageEncoder());
                    }
                });

        try {
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(nettyClientConfig.getIp(), nettyClientConfig.getPort())).sync();
            channel = channelFuture.channel();
        } catch (InterruptedException e) {
            throw new RuntimeException("this.bootstrap.connect().sync() InterruptedException", e);
        }
    }

    @Override
    public void close() {
        if (null != channel) {
            channel.close();
        }

        if (null != bootstrap) {
            eventLoopGroupWork.shutdownGracefully();
        }
    }

    public Channel getChannel() {
        return channel;
    }
}
