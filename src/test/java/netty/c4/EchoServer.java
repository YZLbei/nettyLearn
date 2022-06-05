package netty.c4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.List;

@Slf4j
public class EchoServer {
    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        


        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
                        pipeline.addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf readBuf = (ByteBuf) msg;
                                
                                log.debug("读数据....");
                                log.debug("msg{}",readBuf.toString(Charset.defaultCharset()));
                                ByteBuf writeBuf = ctx.alloc().buffer();
                                writeBuf.writeBytes(readBuf);
                                readBuf.release();
                                ch.writeAndFlush(writeBuf);
                                writeBuf.release();
                                
                                
                            }
                        });
//                        pipeline.addLast(new ChannelOutboundHandlerAdapter(){
//                            @Override
//                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//                                log.debug("写数据...");
//                                log.debug("写数据{}",msg.toString());
//                                ByteBuf buffer = ctx.alloc().buffer();
//                                //如何发送数据
//                                ctx.writeAndFlush(buffer);
//                                buffer.release();
//                                //super.write(ctx, msg, promise);
//                            }
//                        });
                    }
                })
                .bind(8080);
    }
}
