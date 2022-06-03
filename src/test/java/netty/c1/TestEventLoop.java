package netty.c1;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TestEventLoop {
    public static void main(String[] args) {
        //1.创建事件循环组
        EventLoopGroup group = new NioEventLoopGroup(2);//io事件，定时任务，普通任务
        //EventLoopGroup group = new DefaultEventLoopGroup(2);//普通任务，定时任务
        //2.获取下一个循环对象
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());
        
        //3.执行普通任务
//        group.next().submit(()->{//向group提交事件
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            log.debug("ok");
//        });
        log.debug("main");
        
        //4.定时任务
        //第二个参数，开始时间，0表示立即执行
        //第三个参数，间隔时间
        //第四个参数，时间单位
        group.next().scheduleAtFixedRate(()->{
            log.debug("ok");
        },0,1, TimeUnit.SECONDS);
        
    }
}
