package nio.c4;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import static nio.c1.ByteBufferUtil.debugAll;

@Slf4j
public class MultiThreadServer {
    public static void main(String[] args) throws IOException {
        JPasswordField df = new JPasswordField(20);
        df.getPassword();
        Thread.currentThread().setName("boss");//������
        ServerSocketChannel ssc  = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector selector = Selector.open();
        SelectionKey ssckey = ssc.register(selector, 0);
        ssckey.interestOps(SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));
        //1.�����̶�����worker����ʼ��
        worker worker = new worker("worker-0");
        
        while (true){
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                if (key.isAcceptable()){
                    SocketChannel sc = ssc.accept();
                    //Ҫע��scҪע�ᵽworker��selector��
                    //sc.register(selector,SelectionKey.OP_READ);
                    sc.configureBlocking(false);
                    log.debug("before register{}",sc.getRemoteAddress());
                    worker.register(sc);//��boss���������ã�����register����boss�̣߳�ֻ��worker���߳������ˣ�����run�����еĲ���worker�߳�
                    // worker-0,select()����������select()����������worker-0��
                    //Ӧ���������߳���
//                    sc.register(worker.worker,SelectionKey.OP_READ);//������boss��
                    log.debug("after register{}",sc.getRemoteAddress());

                }
            }
        }
    }

    /**
     * worker�������д�¼�
     */
    static class worker implements Runnable{
        private Thread thread;//ÿ��worker�ɶ������߳�
        private Selector worker;//ÿ��worker�ж�����Selector
        private String name;
        //�����̴߳������ݲ����ô�����ĳ��λ��ִ��
        //�̴߳������ݾͿ����ö���
        private ConcurrentLinkedQueue <Runnable> queue = new ConcurrentLinkedQueue<>();
        private volatile boolean start  =false;
        
        
        private JPasswordField ja = new JPasswordField(20);

        public worker(String name) {
            this.name = name;
        }
        //��ʼ���̺߳�Selector
        public void register(SocketChannel sc) throws IOException {
            if (!start){
                worker = Selector.open();
                //��worker����������
                thread = new Thread(this,name);
                
                //�߳�Ҫ����
                thread.start();
                start  = true;
            }
            //���������������񣬵��ǲ�û��ִ��
            queue.add(()->{
                try {
                    sc.register(worker,SelectionKey.OP_READ);//������boss��
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            worker.wakeup();//����select()��������������
           
        }

        @Override
        public void run() {
            while(true){
                try {
                    worker.select();
                    Runnable task = queue.poll();
                    if (task!=null){
                        task.run();//ִ����ע�ᣬ�϶�����worker�߳���
                    }
                    Iterator<SelectionKey> iterator = worker.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isReadable()){
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel channel = (SocketChannel) key.channel();
                            channel.read(buffer);
                            log.debug("read{}",channel.getRemoteAddress());
                            buffer.flip();
                            debugAll(buffer);
                            
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
        }
    }
}
                  