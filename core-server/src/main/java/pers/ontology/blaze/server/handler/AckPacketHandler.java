package pers.ontology.blaze.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.ontology.blaze.packet.handler.PacketBodyHandler;
import pers.ontology.blaze.protocol.TransportProtocol;
import pers.ontology.blaze.protocol.creator.AckCreator;
import pers.ontology.blaze.protocol.utils.MessageIdHelper;
import pers.ontology.blaze.protocol.utils.MessageIdSegment;
import pers.ontology.blaze.server.ChannelRegistry;
import pers.ontology.blaze.server.retry.FailFutureListener;
import pers.ontology.blaze.server.retry.RetryPerformer;
import pers.ontology.blaze.server.server.BlazeServerContext;
import pers.ontology.blaze.utils.LogCharGraph;

/**
 * <h3>应答包处理器</h3>
 *
 * @author ontology
 * @since 1.8
 */
public class AckPacketHandler implements PacketBodyHandler<TransportProtocol.Ack> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AckPacketHandler.class);

    public AckPacketHandler () {
    }

    /**
     * 解析包体
     *
     * @param msg 数据
     * @param ctx 通道
     *
     * @return
     */
    @Override
    public Object parse (TransportProtocol.Ack msg, ChannelHandlerContext ctx) throws Exception {

        if (msg.getType() == TransportProtocol.Ack.Type.CONFIRM_NOTIFY) {
            LOGGER.info("target-client收到我发送的notify" + LogCharGraph.ARROWS + "[" + msg.getMessageId() + "]:");

            //向目标客户端发送我收到 ‘目标客户端确认收到消息’ 的消息(Ack-CCN)
            this.pushTargetClientAckCCN(msg, ctx);

            //向源客户端发送 '消息最终送达' 的消息(Ack-A)
            this.pushSourceClientAckA(msg);

        }

        return null;
    }

    /**
     * 向目标客户端发送我收到 ‘目标客户端确认收到消息’ 的消息(Ack-CCN)
     *
     * @param msg
     * @param ctx
     */
    private void pushTargetClientAckCCN (TransportProtocol.Ack msg, ChannelHandlerContext ctx) {
        //
        TransportProtocol.Ack ack_ccn = AckCreator.get()
                .setType(TransportProtocol.Ack.Type.CONFIRM_CONFIRM_NOTIFY)
                .setTimestamp()
                .setMessageId(msg.getMessageId())
                .done();

        FailFutureListener retryFutureListener = new FailFutureListener(new RetryPerformer(ack_ccn, ctx.channel()));
        ChannelFuture channelFuture = ctx.writeAndFlush(ack_ccn);
        channelFuture.addListener(retryFutureListener);
        channelFuture.addListener(future -> {
            //发送成功
            if (future.cause() == null) {
                LOGGER.info(LogCharGraph.TAB + LogCharGraph.HORIZONTAL_LINE);
                LOGGER.info(LogCharGraph.TAB + "|" + LogCharGraph.ARROWS + "MsgID:" + msg.getMessageId());
                LOGGER.info(
                        LogCharGraph.TAB + "|" + LogCharGraph.ARROWS + "向target-client发送我收到 ‘目标客户端确认收到消息’ 的消息(Ack-CCN)成功！");
                LOGGER.info(LogCharGraph.TAB + LogCharGraph.HORIZONTAL_LINE);
            }
        });
    }

    /**
     * 向源客户端发送 '消息最终送达' 的消息(Ack-A)
     *
     * @param msg
     */
    private void pushSourceClientAckA (TransportProtocol.Ack msg) {
        //消息ID解析结果
        MessageIdSegment segment = MessageIdHelper.parse(msg.getMessageId());

        //获取通道注册表
        ChannelRegistry channelRegistry = BlazeServerContext.getChannelRegistry();
        Channel fromChannel = channelRegistry.findChannel(segment.getFrom());

        //
        TransportProtocol.Ack ack_a = AckCreator.get()
                .setType(TransportProtocol.Ack.Type.ARRIVE)
                .setTimestamp()
                .setMessageId(msg.getMessageId())
                .done();

        FailFutureListener failFutureListener = new FailFutureListener(new RetryPerformer(ack_a, fromChannel));
        ChannelFuture channelFuture = fromChannel.writeAndFlush(ack_a);
        channelFuture.addListener(failFutureListener);
        channelFuture.addListener(future -> {
            //发送成功
            if (future.cause() == null) {
                LOGGER.info(LogCharGraph.TAB + LogCharGraph.HORIZONTAL_LINE);
                LOGGER.info(LogCharGraph.TAB + "|" + LogCharGraph.ARROWS + "MsgID:" + msg.getMessageId());
                LOGGER.info(LogCharGraph.TAB + "|" + LogCharGraph.ARROWS + "向source-client发送 '消息最终送达' 的消息(Ack-A)成功！");
                LOGGER.info(LogCharGraph.TAB + LogCharGraph.HORIZONTAL_LINE);
            }
        });
    }


}
