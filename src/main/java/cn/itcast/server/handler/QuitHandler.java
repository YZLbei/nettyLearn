package cn.itcast.server.handler;

import cn.itcast.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

//��������Ϣ
//ֻ�����쳣�¼���channel inactive�¼�
//���Լ̳�ChannelInboundHandlerAdapter
@Slf4j
@ChannelHandler.Sharable
public class QuitHandler extends ChannelInboundHandlerAdapter {
    //�����ӶϿ�ʱ����inactive�¼�
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SessionFactory.getSession().unbind(ctx.channel());
        log.debug("{}�Ѿ��Ͽ�",ctx.channel());
    }

    
    //��׽ס�쳣ʱ����
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        SessionFactory.getSession().unbind(ctx.channel());
        log.debug("{}�Ѿ��쳣�Ͽ����쳣��Ϣ{}",ctx.channel(),cause);
        
    }
}
