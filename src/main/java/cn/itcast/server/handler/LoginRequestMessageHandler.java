package cn.itcast.server.handler;

import cn.itcast.message.LoginRequestMessage;
import cn.itcast.message.LoginResponseMessage;
import cn.itcast.server.service.UserService;
import cn.itcast.server.service.UserServiceFactory;
import cn.itcast.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        String password = msg.getPassword();
        String username = msg.getUsername();
        UserService service = UserServiceFactory.getUserService();
        boolean login = service.login(username, password);
        LoginResponseMessage response;
        if (login) {
            //���û�����channel��
            SessionFactory.getSession().bind(ctx.channel(), username);
            response = new LoginResponseMessage(true, "��½�ɹ�");
        } else {
            response = new LoginResponseMessage(false, "�û������������");
        }
        ctx.writeAndFlush(response);
    }
}
