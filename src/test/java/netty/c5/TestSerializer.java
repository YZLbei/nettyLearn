package netty.c5;

import cn.itcast.config.Config;
import cn.itcast.message.LoginRequestMessage;
import cn.itcast.message.Message;
import cn.itcast.protocol.MessageCodecSharable;
import cn.itcast.protocol.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;

import java.security.MessageDigest;

/**
 * @Auther: YuZhenLong
 * @Date: 2022/6/15 20:51
 * @Description: 测试序列化算法
 */
public class TestSerializer {
    public static void main(String[] args) {
        MessageCodecSharable CODEC = new MessageCodecSharable();
        LoggingHandler LOGGING = new LoggingHandler();
        EmbeddedChannel channel = new EmbeddedChannel(LOGGING,CODEC,LOGGING);
        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123");
        //channel.writeOutbound(message);
        ByteBuf byteBuf = messageToByteBuf(message);
        //入站解码
        channel.writeInbound(byteBuf);

    }
    public static ByteBuf messageToByteBuf(Message msg) {
        int algorithm = Config.getSerializerAlgorithm().ordinal();
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        out.writeBytes(new byte[]{1, 2, 3, 4});
        out.writeByte(1);
        out.writeByte(algorithm);
        out.writeByte(msg.getMessageType());
        out.writeInt(msg.getSequenceId());
        out.writeByte(0xff);
        byte[] bytes = Serializer.Algorithm.values()[algorithm].serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
        return out;
    }
    
}
