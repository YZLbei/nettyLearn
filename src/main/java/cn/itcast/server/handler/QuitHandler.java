package cn.itcast.server.handler;

import cn.itcast.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

//不关心消息
//只关心异常事件和channel inactive事件
//所以继承ChannelInboundHandlerAdapter
@Slf4j
@ChannelHandler.Sharable
public class QuitHandler extends ChannelInboundHandlerAdapter {
    //当连接断开时触发inactive事件
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SessionFactory.getSession().unbind(ctx.channel());
        log.debug("{}已经断开",ctx.channel());
    }

    
    //捕捉住异常时触发
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        SessionFactory.getSession().unbind(ctx.channel());
        log.debug("{}已经异常断开，异常信息{}",ctx.channel(),cause);
        
    }
}
