package cn.itcast.client.handler;

import cn.itcast.message.RpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: YuZhenLong
 * @Date: 2022/6/16 20:14
 * @Description:
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcResponseMessageHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {
    //                        序号     存放结果的Promise对象   
    //虽然存放状态信息，但是考虑了线程安全问题，可以设置为sharable
    public static final Map<Integer, Promise<Object>>PROMISES = new ConcurrentHashMap<>();
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMessage msg) throws Exception {
        
        log.debug("{}",msg);
        //拿到还未填充结果的promise
        //Promise<Object> promise = PROMISES.get(msg.getSequenceId());
        //promise得到之后消息没用，需要移除
        Promise<Object> promise = PROMISES.remove(msg.getSequenceId());
        
        if (promise!=null){
            Object returnValue = msg.getReturnValue();
            Exception exceptionValue = msg.getExceptionValue();
            if (exceptionValue!=null){
                promise.setFailure(exceptionValue);
            }
            else {
                promise.setSuccess(returnValue);
            }
        }
    }
}
