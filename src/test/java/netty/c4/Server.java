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
        
        //1.����selector
        Selector selector = Selector.open();
        //2.����������
        ServerSocketChannel ssc  = ServerSocketChannel.open();
        //sscҪ������ģʽ
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8080));
        
        //3.��sscע�ᵽSelector
        //key�ǽ����¼�������ͨ��������֪���¼����ĸ�channel���¼�
        SelectionKey ssckey = ssc.register(selector, 0, null);

        //4.selectorֻ��עaccept
        ssckey.interestOps(SelectionKey.OP_ACCEPT);
        
        while(true){
            //5.select������û���¼�����ʱ�����������¼��Ż�ظ�������
            selector.select();
            
            //6.�����¼���selectedKeys�ڲ����������з������¼�
            //������
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                log.debug("key:{}",key);
                //7.�ж��¼�������
                if (key.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    SelectionKey sckey = sc.register(selector, 0, buffer);
                    //ֻ������¼�
                    sckey.interestOps(SelectionKey.OP_READ);
                    

                    log.debug("channel:{}",sc);
                }
                else if(key.isReadable()){
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        int read = channel.read(buffer);//����������Ͽ���readΪ-16
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
