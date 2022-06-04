package netty.c3;


import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static java.lang.Thread.sleep;

@Slf4j
public class TestNettyFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //�̳߳�
        NioEventLoopGroup group = new NioEventLoopGroup();

        EventLoop eventloop = group.next();
        //����execute����Ϊ����ΪRunnable��û�з���ֵ
        Future<Integer> future = eventloop.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("ִ������");
                sleep(1000);
                return 80;
            }
        });
        
        
//        log.debug("�ȴ����");
//        log.debug("{}",future.get());
//        future.get();
        
        future.addListener(new GenericFutureListener<Future<? super Integer>>() {
            @Override
            public void operationComplete(Future<? super Integer> future) throws Exception {
                log.debug("{}",future.getNow());
            }
        });
    }
}
