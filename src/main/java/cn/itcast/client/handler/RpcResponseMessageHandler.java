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
    //                        ���     ��Ž����Promise����   
    //��Ȼ���״̬��Ϣ�����ǿ������̰߳�ȫ���⣬��������Ϊsharable
    public static final Map<Integer, Promise<Object>>PROMISES = new ConcurrentHashMap<>();
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMessage msg) throws Exception {
        
        log.debug("{}",msg);
        //�õ���δ�������promise
        //Promise<Object> promise = PROMISES.get(msg.getSequenceId());
        //promise�õ�֮����Ϣû�ã���Ҫ�Ƴ�
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
