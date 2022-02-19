package org.csp.rpc.remoting.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.csp.rpc.remoting.api.RemotingServer;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class NettyRemotingServer implements RemotingServer {


    private final NettyServerConfig nettyServerConfig;

    private ServerBootstrap serverBootstrap;
    private EventLoopGroup eventLoopGroupBoss;
    private EventLoopGroup eventLoopGroupWork;
    private Channel channel;

    public NettyRemotingServer(NettyServerConfig nettyServerConfig) {
        this.nettyServerConfig = nettyServerConfig;
    }

    @Override
    public void start() {
        if (nettyServerConfig.useEpoll()) {
            eventLoopGroupBoss = new EpollEventLoopGroup(1, new ThreadFactory() {
                private AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyEPOLLBoss_%d", this.threadIndex.incrementAndGet()));
                }
            });

            eventLoopGroupWork = new EpollEventLoopGroup(nettyServerConfig.getWorkThreads(), new ThreadFactory() {
                private AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyEPOLLWork_%d", this.threadIndex.incrementAndGet()));
                }
            });

        } else {
            eventLoopGroupBoss = new NioEventLoopGroup(1, new ThreadFactory() {
                private AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyEPOLLBoss_%d", this.threadIndex.incrementAndGet()));
                }
            });

            eventLoopGroupWork = new NioEventLoopGroup(nettyServerConfig.getWorkThreads(), new ThreadFactory() {
                private AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyEPOLLBoss_%d", this.threadIndex.incrementAndGet()));
                }
            });
        }

        serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(eventLoopGroupBoss, eventLoopGroupWork)
                .channel(nettyServerConfig.useEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, nettyServerConfig.getKeepAlive())
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(0 ,0, nettyServerConfig.getIdleTimeoutMillis(), TimeUnit.MILLISECONDS))
                                .addLast(new MessageDecoder())
                                .addLast(new MessageEncoder());
                    }
                });

        try {
            ChannelFuture channelFuture = serverBootstrap.bind(nettyServerConfig.getPort()).sync();
            channel = channelFuture.channel();
        } catch (InterruptedException e) {
            throw new RuntimeException("this.serverBootstrap.bind().sync() InterruptedException", e);
        }
    }

    @Override
    public void close() {
        if (null != channel) {
            channel.close();
        }

        if (null != serverBootstrap) {
            eventLoopGroupWork.shutdownGracefully();
            eventLoopGroupBoss.shutdownGracefully();
        }

    }
}
