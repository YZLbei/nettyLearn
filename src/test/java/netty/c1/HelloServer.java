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
        //1.������ ,������װnetty���������������
        new ServerBootstrap()
                //2.���� NioEventLoopGroup�����selector��thread����group��
                .group(new NioEventLoopGroup())
                //3.ѡ���������ServerSocketʵ��
                .channel(NioServerSocketChannel.class)
                //4.����worker��child����ִ����д������handler��
                .childHandler(
                        //5.����Ϳͻ��˽�������ͨ�ŵ�ͨ����Initializer,��ʼ������������ӱ��handler
                        new ChannelInitializer<NioSocketChannel>() {//�����ӽ��������
                            @Override
                            protected void initChannel(NioSocketChannel ch) throws Exception {
                                
                                //6.��Ӿ����childHandler
                                ch.pipeline().addLast(new StringDecoder());//��ByteBufferת��Ϊ�ַ���
                                ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){//�Զ���handler

                                    @Override//���¼�
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        //��ӡ��һ��ת���õ��ַ���
                                        System.out.println(msg);
                                    }
                                });
                            }
                        })
                //7.�󶨶˿�
                .bind(8080);
    }
}
