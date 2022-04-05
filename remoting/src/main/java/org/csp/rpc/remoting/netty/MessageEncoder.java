package org.csp.rpc.remoting.netty;

import com.caucho.hessian.io.Hessian2Output;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import javax.swing.*;
import java.io.ByteArrayOutputStream;

/**
 *
 * 协议设计： 消息头 + 消息体
 * 魔术位 1 字节
 * 消息整体长度 4 字节 （除去魔术位）
 * 消息头长度 1 字节
 * 协议版本 1 字节
 * 消息类型 1 字节
 * 序列化方式 1 字节
 * 消息ID  8 字节
 *
 * 协议头扩展字段（不固定）
 *
 * 消息体
 *
 */
public class MessageEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(os);
        output.writeObject(msg);
        output.flushBuffer();

        // 魔术位 1 字节
        out.writeByte(1);

        // 消息总长度

        // 协议体长度
        int bodyLen = os.size();
        int totalLen = 1 + 1 + 1 + 1 + 8 /* 协议头 ??? 扩展字段 */ + bodyLen;
        out.writeInt(totalLen);

        // 消息头长度
        out.writeByte(0);

        // 协议版本
        out.writeByte(1);

        // 消息类型
        out.writeByte(1);

        // 序列化方式
        out.writeByte(1);

        // 消息ID
        out.writeLong(1);

        byte[] bytes = os.toByteArray();
        os.close();

        out.writeBytes(bytes);
    }
}
