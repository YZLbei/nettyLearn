package cn.itcast.client;

import cn.itcast.message.*;
import cn.itcast.protocol.MessageCodecSharable;
import cn.itcast.protocol.ProcotolFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class ChatClient {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        //����ʱ��
        //�̼߳�ͨ��
        CountDownLatch WAIT_FOR_LOGIN = new CountDownLatch(1);
        
        //��CountDownLatch�ж�ʱ��ʾ��Ϣ
        //false��ʾ��δ��¼
        AtomicBoolean LOGIN = new AtomicBoolean(false);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(group);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    //��վ������
                    ch.pipeline().addLast(new ProcotolFrameDecoder());
                    ch.pipeline().addLast(LOGGING_HANDLER);
                    ch.pipeline().addLast(MESSAGE_CODEC);
                    //��Ҫ������վ����������վ��������channel��write֮����ս��������codec������վҲ�г�ս
                    //��վ���������ǽ���ǰ��handler��Ϣ
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                        //������Ӧ��Ϣ
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            log.debug("{}",msg);
                            if ((msg instanceof LoginResponseMessage)) {
                                LoginResponseMessage response = (LoginResponseMessage) msg;
                                if (response.isSuccess()) {
                                    //�����¼�ɹ�
                                    LOGIN.set(true);
                                }
                                //���ӽ�������-1�������û���������̼߳�������
                                //�����Լ�д���߳�
                                WAIT_FOR_LOGIN.countDown();
                            }
                            
                        }

                        /**
                         * ���ӽ��������active�¼�
                         * ���ӽ�����ͻ��������û������뷢�͸�������
                         * @param ctx
                         * @throws Exception
                         */
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            
                            new Thread(()->{
                                Scanner scanner = new Scanner(System.in);
                                System.out.println("�������û�����");
                                String username = scanner.nextLine();
                                System.out.println("����������");
                                String password = scanner.nextLine();
                                LoginRequestMessage login = new LoginRequestMessage(username, password);
                                ctx.writeAndFlush(login);
                                
                                try {
                                    //��Ӧ������������
                                    WAIT_FOR_LOGIN.await();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (!LOGIN.get()){
                                    ctx.channel().close();
                                    return;
                                }
                                else {
                                    while (true){
                                        System.out.println("==================================");
                                        System.out.println("send [username] [content]");
                                        System.out.println("gsend [group name] [content]");
                                        System.out.println("gcreate [group name] [m1,m2,m3...]");
                                        System.out.println("gmembers [group name]");
                                        System.out.println("gjoin [group name]");
                                        System.out.println("gquit [group name]");
                                        System.out.println("quit");
                                        System.out.println("==================================");
                                        String command = scanner.nextLine();
                                        String[] s = command.split(" ");
                                        switch (s[0]){
                                            case"send":
                                               ctx.writeAndFlush(new ChatRequestMessage(username,s[1],s[2]));
                                                break;
                                            case "gsend":
                                                ctx.writeAndFlush(new GroupChatRequestMessage(username,s[1],s[2]));
                                                break;
                                            case "gcreate":
                                                Set <String>member =  new HashSet<String>( Arrays.asList(s[2].split(",")));
                                                member.add(username);
                                                ctx.writeAndFlush(new GroupCreateRequestMessage(s[1],member));
                                                break;
                                            case "gmemebers":
                                                ctx.writeAndFlush(new GroupMembersRequestMessage(s[1]));
                                                break;
                                            case "gjoin":
                                                ctx.writeAndFlush(new GroupJoinRequestMessage(username,s[1]));
                                                break;
                                            case "gquit":
                                                ctx.writeAndFlush(new GroupQuitRequestMessage(username,s[1]));
                                                break;
                                            case "quit":
                                                ctx.channel().close();
                                                break;
                                        }
                                    }
                                }

                            },"system in").start();
                            
                            
                        }
                    });
                }
            });
            Channel channel = bootstrap.connect("localhost", 8080).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("client error", e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
