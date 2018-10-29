package pers.ontology.blaze.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.ontology.blaze.packet.handler.PacketBodyHandler;
import pers.ontology.blaze.protocol.ChatProtocol;
import pers.ontology.blaze.protocol.TransportProtocol;
import pers.ontology.blaze.protocol.TransportProtocol.Ack;
import pers.ontology.blaze.protocol.creator.AckCreator;
import pers.ontology.blaze.protocol.creator.NotifyCreator;
import pers.ontology.blaze.server.ChannelRegistry;
import pers.ontology.blaze.server.retry.FailFutureListener;
import pers.ontology.blaze.server.retry.RetryPerformer;
import pers.ontology.blaze.server.server.BlazeServerContext;
import pers.ontology.blaze.utils.LogCharGraph;

/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
public class MessagePacketHandler implements PacketBodyHandler<ChatProtocol.Message> {


    private static final Logger LOGGER = LoggerFactory.getLogger(MessagePacketHandler.class);

    /**
     * @param message
     * @param ctx     通道
     *
     * @return
     */
    @Override
    public Object parse (ChatProtocol.Message message, ChannelHandlerContext ctx) {

        String from = message.getFrom();
        String to = message.getTo();
        //        String body = message.getBody();
        LOGGER.info("我收到来自source-client的消息" + LogCharGraph.ARROWS + "[" + message.getId() + "]:");
        //获取通道注册表
        ChannelRegistry channelRegistry = BlazeServerContext.getChannelRegistry();

        //确认消息
        this.acknowledge(message, channelRegistry);


        //转发消息
        this.relay(message, channelRegistry);

        return null;
    }

    /**
     * 转发
     *
     * <p> 将消息转发到目标客户端
     *
     * @param message         消息
     * @param channelRegistry
     */
    private void relay (ChatProtocol.Message message, ChannelRegistry channelRegistry) {

        TransportProtocol.Notify notify = NotifyCreator.get().setBody(message).done();

        String to = message.getTo();
        //目标端在线
        if (channelRegistry.isExist(to)) {
            //向目标端发送Notify
            Channel toChannel = channelRegistry.findChannel(to);
            ChannelFuture channelFuture = toChannel.writeAndFlush(notify);


//            FailFutureListener failFutureListener = new FailFutureListener(new RetryPerformer(notify, toChannel));
//            channelFuture.addListener(failFutureListener);
            channelFuture.addListener(future -> {
                //发送成功
                if (future.cause() == null) {
                    LOGGER.info(LogCharGraph.TAB + LogCharGraph.HORIZONTAL_LINE);
                    LOGGER.info(LogCharGraph.TAB + "|" + LogCharGraph.ARROWS + "MsgID:" + message.getId());
                    LOGGER.info(LogCharGraph.TAB + "|" + LogCharGraph.ARROWS + "向target-client发送的消息(Notify)成功！");
                    LOGGER.info(LogCharGraph.TAB + LogCharGraph.HORIZONTAL_LINE);
                }
            });
        }
        //目标端不在线
        else {
            //TODO:1、将消息内容缓存
            //TODO:2、向发送端回执ACK-ARRIVE

            LOGGER.warn("用户" + to + "已离线，消息内容将缓存");
        }

    }

    /**
     * 向发送端应答ACK-CONFIRM
     *
     * <p>代表着服务器已收到来自客户端的消息
     *
     * @param message
     * @param channelRegistry
     */
    private void acknowledge (ChatProtocol.Message message, ChannelRegistry channelRegistry) {

        String messageId = message.getId();
        Ack ack_c = AckCreator.get().setType(Ack.Type.CONFIRM).setTimestamp().setMessageId(messageId).done();

        //向请求端回执Ack
        Channel fromChannel = channelRegistry.findChannel(message.getFrom());
        ChannelFuture channelFuture = fromChannel.writeAndFlush(ack_c);


//        FailFutureListener failFutureListener = new FailFutureListener(new RetryPerformer(ack_c, fromChannel));
//        channelFuture.addListener(failFutureListener);
        channelFuture.addListener(future -> {
            //发送成功
            if (future.cause() == null) {
                LOGGER.info(LogCharGraph.TAB + LogCharGraph.HORIZONTAL_LINE);
                LOGGER.info(LogCharGraph.TAB + "|" + LogCharGraph.ARROWS + "MsgID:" + messageId);
                LOGGER.info(
                        LogCharGraph.TAB + "|" + LogCharGraph.ARROWS + "向source-client发送我收到 '你发送的消息' 的消息(Ack-C)成功！");
                LOGGER.info(LogCharGraph.TAB + LogCharGraph.HORIZONTAL_LINE);
            }
        });
    }
}
