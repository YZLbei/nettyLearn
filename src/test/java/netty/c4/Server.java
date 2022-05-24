package netty.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static netty.c1.ByteBufferUtil.debugAll;
import static netty.c1.ByteBufferUtil.debugRead;

@Slf4j
public class Server {
    public static void split(ByteBuffer buffer){
        buffer.flip();
        for (int i = 0; i <buffer.limit();i++){ ;
            if (buffer.get(i)=='\n'){
                int length = i+1-buffer.position();
                ByteBuffer target  = ByteBuffer.allocate(length);
                for (int j = 0; j < length; j++) {
                    byte b = buffer.get();
                    target.put(b);
                }
                target.flip();
                debugAll(target);
            }
        }
        buffer.compact();
    }
    public static void main(String[] args) throws IOException {
        
        //1.创建selector
        Selector selector = Selector.open();
        //2.创建服务器
        ServerSocketChannel ssc  = ServerSocketChannel.open();
        //ssc要非阻塞模式
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8080));
        
        //3.将ssc注册到Selector
        //key是将来事件发生后，通过它可以知道事件和哪个channel的事件
        SelectionKey ssckey = ssc.register(selector, 0, null);

        //4.selector只关注accept
        ssckey.interestOps(SelectionKey.OP_ACCEPT);
        
        while(true){
            //5.select方法，没有事件发生时是阻塞，右事件才会回复下运行
            selector.select();
            
            //6.处理事件，selectedKeys内部包含了所有发生的事件
            //迭代器
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                log.debug("key:{}",key);
                //7.判断事件的类型
                if (key.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    SelectionKey sckey = sc.register(selector, 0, buffer);
                    //只处理读事件
                    sckey.interestOps(SelectionKey.OP_READ);
                    

                    log.debug("channel:{}",sc);
                }
                else if(key.isReadable()){
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        int read = channel.read(buffer);//如果是正常断开，read为-16
                        if(read==-1){
                            key.cancel();
                        }
                        else {
                            split(buffer);
                            if (buffer.position()==buffer.limit()){
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity()*2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                key.attach(newBuffer);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        key.cancel();
                    }
                }
                iterator.remove();
            }
        }
    }
}
