package itacast.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import itacast.message.LoginRequestMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestMessageCodec {
    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(
                //需要定义帧解码器解决年报半包的问题
                //要在LoggingHandler前
                new LengthFieldBasedFrameDecoder(1024,12,4,0,0),
                new LoggingHandler(LogLevel.DEBUG),
                new MessageCodec()
        );
        //encode
        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123");
        channel.writeOutbound(message);
        //channel.writeInbound();
        
        
        //decode
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        try {
            new MessageCodec().encode(null,message,buf);
            //channel.writeInbound(buf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ByteBuf slice1 = buf.slice(0, 100);
        ByteBuf slice2 = buf.slice(100, buf.readableBytes() - 100);
        slice1.retain();
        channel.writeInbound(slice1);
        channel.writeInbound(slice2);

    }
    
    
}
