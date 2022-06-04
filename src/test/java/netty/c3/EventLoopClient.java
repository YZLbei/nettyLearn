package netty.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;

@Slf4j
public class EventLoopClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                //5. ���ӵ�������
                .connect(new InetSocketAddress("localhost", 8080));


        Channel channel = channelFuture.channel();
        log.debug(String.valueOf(channel));
        new Thread(()->{
           Scanner scanner = new Scanner(System.in);
            //String line  = scanner.nextLine();
           while (true){
               String line  = scanner.nextLine();
               if (line.equals("q")){
                   channel.close();
                   break;
               }
               channel.writeAndFlush(line);
           }
        },"input").start();
        
        
        
        //closeFuture
        //�Ȼ�ȡcloseFuture
        ChannelFuture closeFuture = channel.closeFuture();

        //����һͬ��
//        ChannelFuture closeFuture = channel.closeFuture();
//        log.debug("waiting close");
//        closeFuture.sync();
//        log.debug("����رպ�Ĳ���");
        
        
        //������
        closeFuture.addListener((ChannelFutureListener) channelFuture1 -> {
            log.debug("����رպ�Ĳ���");
            //���ŵ�
            group.shutdownGracefully();
        });
        

    }
}
