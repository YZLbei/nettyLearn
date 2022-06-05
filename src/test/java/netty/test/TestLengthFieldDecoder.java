package netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class TestLengthFieldDecoder {
    public static void main(String[] args) {
        /**
         * 相当于服务器
         */
        EmbeddedChannel channel  = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024,0,4,1,4),
                new LoggingHandler(LogLevel.DEBUG)
        );
        ByteBuf buffer  = ByteBufAllocator.DEFAULT.buffer();
        send(buffer, "hello,world");
        send(buffer, "hi");
        send(buffer, "nihao");
        channel.writeInbound(buffer);
    }

    private static void send(ByteBuf buffer, String s) {
        byte[]bytes = s.getBytes();
        int length = bytes.length;//实际内容长度
        buffer.writeInt(length);
        buffer.writeByte(1);
        buffer.writeBytes(bytes);
    }
}
