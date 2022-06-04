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
        //线程池
        NioEventLoopGroup group = new NioEventLoopGroup();

        EventLoop eventloop = group.next();
        //不用execute，因为参数为Runnable，没有返回值
        Future<Integer> future = eventloop.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("执行运算");
                sleep(1000);
                return 80;
            }
        });
        
        
//        log.debug("等待结果");
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
