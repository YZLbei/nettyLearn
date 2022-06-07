package itcast.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import itcast.message.Message;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

@ChannelHandler.Sharable
@Slf4j
/**
 * �����LengthFieldBasedFrameһ��ʹ��
 */
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> outlist) throws Exception {
        ByteBuf out = ctx.alloc().buffer();

        // 1. 4 �ֽڵ�ħ��
        out.writeBytes(new byte[]{1, 2, 3, 4});
        // 2. 1 �ֽڵİ汾,
        out.writeByte(1);
        // 3. 1 �ֽڵ����л���ʽ jdk 0 , json 1
        out.writeByte(0);
        // 4. 1 �ֽڵ�ָ������
        out.writeByte(msg.getMessageType());
        // 5. 4 ���ֽ�
        out.writeInt(msg.getSequenceId());
        // �����壬�������
        out.writeByte(0xff);
        // 6. ��ȡ���ݵ��ֽ�����
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(msg);
        byte[] bytes = bos.toByteArray();
        // 7. ����
        out.writeInt(bytes.length);
        // 8. д������
        out.writeBytes(bytes);
        
        outlist.add(out);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        ByteBuf in = ctx.alloc().buffer();
        int magicNum = in.readInt();
        byte version = in.readByte();
        byte serializerType = in.readByte();
        byte messageType = in.readByte();
        int sequenceId = in.readInt();
        in.readByte();
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Message message = (Message) ois.readObject();
        log.debug("{}, {}, {}, {}, {}, {}", magicNum, version, serializerType, messageType, sequenceId, length);
        log.debug("{}", message);
        out.add(message);
    }
}