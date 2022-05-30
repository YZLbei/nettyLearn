package netty.c1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class HelloServer {
    public static void main(String[] args) {
        //1.启动器 ,负责组装netty组件，启动服务器
        new ServerBootstrap()
                //2.加入 NioEventLoopGroup组件（selector，thread），group组
                .group(new NioEventLoopGroup())
                //3.选择服务器的ServerSocket实现
                .channel(NioServerSocketChannel.class)
                //4.决定worker（child）能执行哪写操作（handler）
                .childHandler(
                        //5.代表和客户端进行数据通信的通道，Initializer,初始化器，负责添加别的handler
                        new ChannelInitializer<NioSocketChannel>() {//在连接建立后调用
                            @Override
                            protected void initChannel(NioSocketChannel ch) throws Exception {
                                
                                //6.添加具体的childHandler
                                ch.pipeline().addLast(new StringDecoder());//将ByteBuffer转化为字符串
                                ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){//自定义handler

                                    @Override//读事件
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        //打印上一步转换好的字符串
                                        System.out.println(msg);
                                    }
                                });
                            }
                        })
                //7.绑定端口
                .bind(8080);
    }
}
