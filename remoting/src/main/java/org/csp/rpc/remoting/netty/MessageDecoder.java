package org.csp.rpc.remoting.netty;

import com.caucho.hessian.io.Hessian2Input;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf input, List<Object> out) throws Exception {
        // 魔术位 + 消息总长度
        if (input.readableBytes() < 5) {
            return;
        }

        input.markReaderIndex();

        byte magic = input.readByte();

        if (magic != 1) {
            // todo 抛异常
            throw new RuntimeException("???? magic 不正确");
        }

        int totalLen = input.readInt();
        if (totalLen <= 0) {
            ctx.close();
        }

        if (input.readableBytes() < totalLen) {
            input.resetReaderIndex();
            return;
        }

        // 消息头长度
        int headLen = input.readByte();

        // 协议版本
        byte version = input.readByte();

        // 消息类型
        byte msgType = input.readByte();

        // 序列化方式
        byte serializerType = input.readByte();

        // 消息ID
        long msgId = input.readLong();

        if (headLen != 0) {
            ByteBuf head = input.readBytes(headLen);
            byte[] array = head.array();
            String headMsg = new String(array, StandardCharsets.UTF_8);
        }

        int msgLen = totalLen - 1 - headLen - 1 - 1 - 1 - 8;

        byte[] bytes = new byte[msgLen];
        input.readBytes(bytes);

        // todo 压缩，解压缩

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Hessian2Input hessian2Input = new Hessian2Input(bis);
        Object obj = hessian2Input.readObject();

        System.out.println(obj);
        out.add(obj);
    }
}
