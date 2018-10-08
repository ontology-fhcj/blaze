package pers.ontology.blaze.packet.coder;

import com.google.protobuf.MessageLite;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.ontology.blaze.packet.PacketHelper;
import pers.ontology.blaze.packet.xml.ProtobufXmlConfig;
import pers.ontology.blaze.utils.LogCharGraph;

/**
 * <h3>多种消息类型编码器</h3>
 *
 * 在报文头部4个byte中的最后一个byte用来识别消息类型，旨在在接收消息的时候提高动态性
 *
 * @author ontology
 * @since 1.8
 */
public class MultiProtobufEncoder extends MessageToByteEncoder<MessageLite> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiProtobufEncoder.class);

    @Override
    protected void encode (ChannelHandlerContext ctx, MessageLite msg, ByteBuf out) throws Exception {

        LOGGER.debug(LogCharGraph.HORIZONTAL_LINE);

        byte[] body = msg.toByteArray();
        byte[] header = encodeHeader(msg, (short) body.length);

        out.writeBytes(header);
        out.writeBytes(body);

        LOGGER.debug("|" + LogCharGraph.ARROWS + "编码(E)：报文长度:" + out.readableBytes());
        LOGGER.debug(LogCharGraph.HORIZONTAL_LINE);
    }

    /**
     * 头部编码
     *
     * @param msg        消息
     * @param bodyLength
     *
     * @return
     *
     * @throws ClassNotFoundException
     */
    private byte[] encodeHeader (MessageLite msg, short bodyLength) throws ClassNotFoundException {


        byte messageType = 0x0f;
        try {
            //根据消息类型在报文头部添加标识
            ProtobufXmlConfig protobufXmlConfig = PacketHelper.getProtobufXmlConfig();
            messageType = protobufXmlConfig.findHexadecimal(msg.getClass());
        } catch (Exception e) {
            LOGGER.error("编码(E)：添加消息类型报头失败:", e);
        }

        byte[] header = new byte[4];
        header[0] = (byte) (bodyLength & 0xff);
        header[1] = (byte) ((bodyLength >> 8) & 0xff);
        header[2] = 0; //
        header[3] = messageType;//消息类型

        LOGGER.debug("|" + LogCharGraph.ARROWS + "编码(E)：添加消息类型报头：数字标识" + messageType);
        LOGGER.debug("|" + LogCharGraph.ARROWS + "编码(E)：消息类型       ：" + msg.getClass().getName());
        return header;

    }
}