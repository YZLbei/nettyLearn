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
        Thread.currentThread().setName("boss");//改名字
        ServerSocketChannel ssc  = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector selector = Selector.open();
        SelectionKey ssckey = ssc.register(selector, 0);
        ssckey.interestOps(SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));
        //1.创建固定数量worker并初始化
        worker worker = new worker("worker-0");
        
        while (true){
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                if (key.isAcceptable()){
                    SocketChannel sc = ssc.accept();
                    //要注意sc要注册到worker的selector中
                    //sc.register(selector,SelectionKey.OP_READ);
                    sc.configureBlocking(false);
                    log.debug("before register{}",sc.getRemoteAddress());
                    worker.register(sc);//被boss方法所调用，所以register中是boss线程，只有worker的线程启动了，它的run方法中的才是worker线程
                    // worker-0,select()方法阻塞，select()方法运行在worker-0中
                    //应该先启动线程再
//                    sc.register(worker.worker,SelectionKey.OP_READ);//运行在boss中
                    log.debug("after register{}",sc.getRemoteAddress());

                }
            }
        }
    }

    /**
     * worker负责检测读写事件
     */
    static class worker implements Runnable{
        private Thread thread;//每个worker由独立的线程
        private Selector worker;//每个worker有独立的Selector
        private String name;
        //两个线程传递数据并且让代码在某个位置执行
        //线程传输数据就可以用队列
        private ConcurrentLinkedQueue <Runnable> queue = new ConcurrentLinkedQueue<>();
        private volatile boolean start  =false;
        
        
        private JPasswordField ja = new JPasswordField(20);

        public worker(String name) {
            this.name = name;
        }
        //初始化线程和Selector
        public void register(SocketChannel sc) throws IOException {
            if (!start){
                worker = Selector.open();
                //把worker本身当成任务
                thread = new Thread(this,name);
                
                //线程要启动
                thread.start();
                start  = true;
            }
            //像队列中添加了任务，但是并没有执行
            queue.add(()->{
                try {
                    sc.register(worker,SelectionKey.OP_READ);//运行在boss中
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            worker.wakeup();//唤醒select()方法，避免阻塞
           
        }

        @Override
        public void run() {
            while(true){
                try {
                    worker.select();
                    Runnable task = queue.poll();
                    if (task!=null){
                        task.run();//执行了注册，肯定是在worker线程中
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
                  