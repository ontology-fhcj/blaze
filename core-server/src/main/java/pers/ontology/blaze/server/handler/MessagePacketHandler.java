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

        //向请求端回执Ack
        Channel fromChannel = channelRegistry.findChannel(from);

        Ack ack = AckCreator.get().setType(Ack.Type.CONFIRM).setTimestamp().done();
        fromChannel.writeAndFlush(ack);


        //向目标端发送Notify
        Channel channel = channelRegistry.findChannel(to);

        TransportProtocol.Notify notify = NotifyCreator.get().setBody(message).done();
        channel.writeAndFlush(notify);


        return null;
    }
}
