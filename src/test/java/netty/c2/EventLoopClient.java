package netty.c2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;


public class EventLoopClient {
    public static void main(String[] args) throws InterruptedException {
        ChannelFuture channel = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                //5. ���ӵ�������
                .connect(new InetSocketAddress("localhost", 8080));
                
        
        //channel.sync();
        channel.addListener(new ChannelFutureListener() {
            //��nio�߳����ӽ������Ժ�����operationComplete
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                Channel channel = channelFuture.channel();
                channel.writeAndFlush("hello");

            }
        });
        //channel.channel();
        //.writeAndFlush("hello world");
                //6.�������������
        System.out.println(channel);
        System.out.println("");
    }
}
