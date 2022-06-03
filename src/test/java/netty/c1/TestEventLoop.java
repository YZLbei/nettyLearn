package netty.c1;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TestEventLoop {
    public static void main(String[] args) {
        //1.�����¼�ѭ����
        EventLoopGroup group = new NioEventLoopGroup(2);//io�¼�����ʱ������ͨ����
        //EventLoopGroup group = new DefaultEventLoopGroup(2);//��ͨ���񣬶�ʱ����
        //2.��ȡ��һ��ѭ������
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());
        
        //3.ִ����ͨ����
//        group.next().submit(()->{//��group�ύ�¼�
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            log.debug("ok");
//        });
        log.debug("main");
        
        //4.��ʱ����
        //�ڶ�����������ʼʱ�䣬0��ʾ����ִ��
        //���������������ʱ��
        //���ĸ�������ʱ�䵥λ
        group.next().scheduleAtFixedRate(()->{
            log.debug("ok");
        },0,1, TimeUnit.SECONDS);
        
    }
}
