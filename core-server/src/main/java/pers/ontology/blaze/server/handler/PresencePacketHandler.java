package pers.ontology.blaze.server.handler;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.ontology.blaze.packet.handler.PacketBodyHandler;
import pers.ontology.blaze.protocol.ChatProtocol;
import pers.ontology.blaze.server.ChannelRegistry;
import pers.ontology.blaze.server.server.BlazeServerContext;

/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
public class PresencePacketHandler implements PacketBodyHandler<ChatProtocol.Presence> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PresencePacketHandler.class);


    @Override
    public Object parse (ChatProtocol.Presence presence, ChannelHandlerContext ctx) {

        ChatProtocol.Presence.Action action = presence.getAction();

        ChannelRegistry channelRegistry = BlazeServerContext.getChannelRegistry();

        //登录
        if (action == ChatProtocol.Presence.Action.LOGIN) {
            channelRegistry.addChannel(presence.getUid(), ctx.channel());
            LOGGER.info("用户{}登录成功！", presence.getUid());
        }

        //退出登录
        if (action == ChatProtocol.Presence.Action.LOGOUT) {
            channelRegistry.removeChannel(presence.getUid());
            LOGGER.info("用户{}退出登录成功！", presence.getUid());
        }

        return null;
    }


}
