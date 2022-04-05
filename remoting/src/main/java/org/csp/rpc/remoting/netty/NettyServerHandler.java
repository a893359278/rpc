package org.csp.rpc.remoting.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelDuplexHandler {

    private org.csp.rpc.remoting.api.ChannelHandler channelHandler;

    public NettyServerHandler(org.csp.rpc.remoting.api.ChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        this.channelHandler.read(msg);
    }


}
