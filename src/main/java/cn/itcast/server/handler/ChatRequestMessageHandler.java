package cn.itcast.server.handler;


import cn.itcast.message.ChatRequestMessage;
import cn.itcast.message.ChatResponseMessage;
import cn.itcast.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {
   
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        String from = msg.getFrom();
        String to = msg.getTo();
        String content = msg.getContent();

        //找到发送方的channel
        Channel channel = SessionFactory.getSession().getChannel(to);
        
        //对方在线
        if (channel!=null){
            channel.writeAndFlush(new ChatResponseMessage(from,content));
        }
        //不在线
        else {
            ctx.writeAndFlush(new ChatResponseMessage(false,"对方不在线"));
        }

    }
}
