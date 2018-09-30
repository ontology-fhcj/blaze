package pers.ontology.blaze.packet.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h3>分发的通道处理器</h3>
 *
 * @author ontology
 * @since 1.8
 */
public class DispatchChannelHandler extends SimpleChannelInboundHandler<Object> {


    private static final Logger LOGGER = LoggerFactory.getLogger(DispatchChannelHandler.class);

    /**
     * <strong>Please keep in mind that this method will be renamed to
     * {@code messageReceived(ChannelHandlerContext, I)} in 5.0.</strong>
     *
     * Is called for each message of type {@link I}.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param msg the message to handle
     *
     * @throws Exception is thrown if an error occurred
     */
    @Override
    protected void channelRead0 (ChannelHandlerContext ctx, Object msg) throws Exception {
        try {


            PacketBodyHandlerRegistry handlerRegistry = HandlerContext.getPacketBodyHandlerRegistry();
            PacketBodyHandler packetBodyHandler = handlerRegistry.findPacketBodyHandler(msg.getClass());


            packetBodyHandler.parse(msg, ctx);
        } catch (Exception e) {
            LOGGER.info(":", e);
        }

    }
}
