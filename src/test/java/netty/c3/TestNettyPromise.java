package netty.c3;

import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

import static java.lang.Thread.sleep;

@Slf4j
public class TestNettyPromise {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1.准备EventLoop对象
        EventLoopGroup group = new NioEventLoopGroup();
        EventLoop eventloop = group.next();


        //2.可以主动的创建promise，而不是被动的提交任务之后创建,是结果容器
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventloop);
        
        new Thread(()->{
            //3.任意一个线程执行运算，计算完毕后向promise中填充结果
            
            log.debug("开始计算");
            try {
                int i =1/0;
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                promise.setFailure(e);
            }
            promise.setSuccess(80);
        }).start();
        log.debug("等待结果");
        log.debug("{}",promise.get());
    }
    
    
}
