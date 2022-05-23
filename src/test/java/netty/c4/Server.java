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
        ssc.configureBlocking(false);//非阻塞模式,影响的是accept方法
        
        //2.绑定端口
        ssc.bind(new InetSocketAddress(8080));
        
        //3.连接集合
        List<SocketChannel>channels = new LinkedList<>();
        
        while(true){
            //4.建立连接
            log.debug("Connecting:");
            SocketChannel accept = ssc.accept();
            if (accept!=null) {
                log.debug("Connected:{}", accept);
                accept.configureBlocking(false);//将read方法设置为非阻塞的
                //非阻塞模式下，线程会继续运行如果没有连接建立，accept为null 
                channels.add(accept);//阻塞方法，会让线程暂停，线程停止运行

            }
            
            for (SocketChannel channel : channels) {
                log.debug("before read{}",channel);
                
                //设置成非阻塞，线程会继续运行，如果没有读到数据read返回0
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
