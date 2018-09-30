package pers.ontology.blaze.server.handler;

import com.google.protobuf.Any;
import io.netty.channel.ChannelHandlerContext;
import pers.ontology.blaze.packet.handler.HandlerContext;
import pers.ontology.blaze.packet.handler.PacketBodyHandler;
import pers.ontology.blaze.packet.handler.PacketBodyHandlerRegistry;
import pers.ontology.blaze.protocol.ChatProtocol;
import pers.ontology.blaze.protocol.TransportProtocol;
import pers.ontology.blaze.protocol.TransportProtocol.Header;

/**
 * <h3>请求包处理器</h3>
 *
 * @author ontology
 * @since 1.8
 */
public class RequestPacketHandler implements PacketBodyHandler<TransportProtocol.Request> {
    /**
     * 解析请求包体
     *
     * @param msg 数据
     * @param ctx 通道
     *
     * @return
     */
    @Override
    public Object parse (TransportProtocol.Request msg, ChannelHandlerContext ctx) throws Exception {
        Header header = msg.getHeader();
        //获取消息体
        Any generic = msg.getBody();

        //判断消息体类型
        //这里目前是if判断，如何动态判断？
        Class<?> clazz = null;
        Object body = null;
        if (generic.is(ChatProtocol.Message.class)) {
            clazz = ChatProtocol.Message.class;
            body = generic.unpack(ChatProtocol.Message.class);
        }
        if (generic.is(ChatProtocol.Presence.class)) {
            clazz = ChatProtocol.Presence.class;
            body = generic.unpack(ChatProtocol.Presence.class);
        }

        if (clazz == null) {
            return null;
        }

        PacketBodyHandlerRegistry packetBodyHandlerRegistry = HandlerContext.getPacketBodyHandlerRegistry();
        PacketBodyHandler packetBodyHandler = packetBodyHandlerRegistry.findPacketBodyHandler(clazz);

        packetBodyHandler.parse(body, ctx);

        return null;
    }
}
