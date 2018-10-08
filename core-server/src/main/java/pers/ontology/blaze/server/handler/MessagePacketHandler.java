package pers.ontology.blaze.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import pers.ontology.blaze.packet.handler.PacketBodyHandler;
import pers.ontology.blaze.protocol.ChatProtocol;
import pers.ontology.blaze.protocol.TransportProtocol;
import pers.ontology.blaze.protocol.creator.AckCreator;
import pers.ontology.blaze.protocol.creator.NotifyCreator;
import pers.ontology.blaze.server.ChannelRegistry;
import pers.ontology.blaze.server.server.BlazeServerContext;
import pers.ontology.blaze.protocol.TransportProtocol.Ack;

/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
public class MessagePacketHandler implements PacketBodyHandler<ChatProtocol.Message> {


    @Override
    public Object parse (ChatProtocol.Message message, ChannelHandlerContext ctx) {

        String from = message.getFrom();
        String to = message.getTo();
        String body = message.getBody();

        //获取通道注册表
        ChannelRegistry channelRegistry = BlazeServerContext.getChannelRegistry();

        //确认消息
        this.acknowledge(from, channelRegistry);


        //转发消息
        this.relay(message, to, channelRegistry);

        return null;
    }

    /**
     * 转发
     *
     * <p> 将消息转发到目标客户端
     *
     * @param message         消息
     * @param to              目的地
     * @param channelRegistry
     */
    private void relay (ChatProtocol.Message message, String to, ChannelRegistry channelRegistry) {
        //向目标端发送Notify
        Channel channel = channelRegistry.findChannel(to);

        TransportProtocol.Notify notify = NotifyCreator.get().setBody(message).done();
        channel.writeAndFlush(notify);
    }

    /**
     * 应答
     *
     * <p>代表着服务器已受到来自客户端的消息
     *
     * @param from
     * @param channelRegistry
     */
    private void acknowledge (String from, ChannelRegistry channelRegistry) {
        //向请求端回执Ack
        Channel fromChannel = channelRegistry.findChannel(from);

        Ack ack = AckCreator.get().setType(Ack.Type.CONFIRM).setTimestamp().done();
        fromChannel.writeAndFlush(ack);
    }
}
