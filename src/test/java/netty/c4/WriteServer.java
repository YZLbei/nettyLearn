package netty.c4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

public class WriteServer {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        SelectionKey ssckey = ssc.register(selector, SelectionKey.OP_ACCEPT, 0);
        ssc.bind(new InetSocketAddress(8080));
        while (true){
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = (SocketChannel) channel.accept();
                    //SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);

                    
                    SelectionKey sckey = sc.register(selector, SelectionKey.OP_READ, 0);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 3000000; i++) {
                        sb.append("a");
                    }

                    ByteBuffer buffer = Charset.defaultCharset().encode(sb.toString());
                    
//                    while(buffer.hasRemaining()){
//                        int write =sc.write(buffer);
//                        System.out.println(write);
//                    }
                    int write = sc.write(buffer);
                    System.out.println(write);
                    if(buffer.hasRemaining()){
                        sckey.interestOps(sckey.interestOps()+SelectionKey.OP_WRITE);
                        sckey.attach(buffer);
                    }
                }
                else if (key.isWritable()){
                   ByteBuffer buffer = (ByteBuffer) key.attachment();
                   SocketChannel sc = (SocketChannel) key.channel();
                    int write = sc.write(buffer);
                    System.out.println(write);
                    if (!buffer.hasRemaining()){
                       key.interestOps(key.interestOps()-SelectionKey.OP_WRITE);
                       key.attach(0);
                   }
                }
            }
            //iterator.remove();
        }
    }
}
