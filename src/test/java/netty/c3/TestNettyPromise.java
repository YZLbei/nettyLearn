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
        //1.׼��EventLoop����
        EventLoopGroup group = new NioEventLoopGroup();
        EventLoop eventloop = group.next();


        //2.���������Ĵ���promise�������Ǳ������ύ����֮�󴴽�,�ǽ������
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventloop);
        
        new Thread(()->{
            //3.����һ���߳�ִ�����㣬������Ϻ���promise�������
            
            log.debug("��ʼ����");
            try {
                int i =1/0;
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                promise.setFailure(e);
            }
            promise.setSuccess(80);
        }).start();
        log.debug("�ȴ����");
        log.debug("{}",promise.get());
    }
    
    
}
