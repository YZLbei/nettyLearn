package netty.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

import static netty.c1.ByteBufferUtil.debugRead;

@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        //ʹ��nio�������ģʽ
        
        //0.ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(16);
        
        //1.����������
        ServerSocketChannel ssc  = ServerSocketChannel.open();
        
        //2.�󶨶˿�
        ssc.bind(new InetSocketAddress(8080));
        
        //3.���Ӽ���
        List<SocketChannel>channels = new LinkedList<>();
        
        while(true){
            //4.��������
            log.debug("Connecting:");
            SocketChannel accept = ssc.accept();
            log.debug("Connected:{}",accept);
            channels.add(accept);//���������������߳���ͣ���߳�ֹͣ����

            for (SocketChannel channel : channels) {
                log.debug("before read{}",channel);
                channel.read(buffer);//read����Ҳ���������߳�Ҳ��ֹͣ����
                buffer.flip();
                debugRead(buffer);
                buffer.clear();
                log.debug("after read",channel);
//                channel.write(buffer);
            }
        }
    }
}
