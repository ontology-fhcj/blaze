package pers.ontology.blaze.server.handler;

import io.netty.channel.Channel;
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
        LOGGER.debug("-消息发送:" + from + LogCharGraph.ARROWS + to);

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

        //向目标端发送Notify
        Channel toChannel = channelRegistry.findChannel(message.getTo());
        toChannel.writeAndFlush(notify);
    }

    /**
     * 应答
     *
     * <p>代表着服务器已收到来自客户端的消息
     *
     * @param message
     * @param channelRegistry
     */
    private void acknowledge (ChatProtocol.Message message, ChannelRegistry channelRegistry) {

        Ack ack = AckCreator.get().setType(Ack.Type.CONFIRM).setTimestamp().done();

        //向请求端回执Ack
        Channel fromChannel = channelRegistry.findChannel(message.getFrom());
        fromChannel.writeAndFlush(ack);
    }
}
