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
        //使用nio理解阻塞模式
        
        //0.ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(16);
        
        //1.创建服务器
        ServerSocketChannel ssc  = ServerSocketChannel.open();
        
        //2.绑定端口
        ssc.bind(new InetSocketAddress(8080));
        
        //3.连接集合
        List<SocketChannel>channels = new LinkedList<>();
        
        while(true){
            //4.建立连接
            log.debug("Connecting:");
            SocketChannel accept = ssc.accept();
            log.debug("Connected:{}",accept);
            channels.add(accept);//阻塞方法，会让线程暂停，线程停止运行

            for (SocketChannel channel : channels) {
                log.debug("before read{}",channel);
                channel.read(buffer);//read方法也是阻塞，线程也会停止运行
                buffer.flip();
                debugRead(buffer);
                buffer.clear();
                log.debug("after read",channel);
//                channel.write(buffer);
            }
        }
    }
}
