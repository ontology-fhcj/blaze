package pers.ontology.blaze.packet.coder;

import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.ontology.blaze.packet.PacketHelper;
import pers.ontology.blaze.packet.xml.ProtobufXmlConfig;
import pers.ontology.blaze.utils.LogCharGraph;

import java.lang.reflect.Method;
import java.util.List;

/**
 * <h3>多种消息类型解码器</h3>
 *
 * <p>其中解决了半包、粘包问题
 *
 * @author ontology
 * @since 1.8
 */
public class MultiProtobufDecoder extends ByteToMessageDecoder {


    private static final Logger LOGGER = LoggerFactory.getLogger(MultiProtobufDecoder.class);

    @Override
    protected void decode (ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        LOGGER.debug(LogCharGraph.HORIZONTAL_LINE);
        LOGGER.debug("|" + LogCharGraph.ARROWS + "解码：报文可读长度:" + in.readableBytes());

        while (in.readableBytes() > 4) { // 如果可读长度小于包头长度，退出。

            in.markReaderIndex();

            // 获取包头中的body长度
            byte low = in.readByte();
            byte high = in.readByte();
            short s0 = (short) (low & 0xff);
            short s1 = (short) (high & 0xff);
            s1 <<= 8;
            short length = (short) (s0 | s1);

            // 获取包头中的protobuf类型
            in.readByte();
            byte dataType = in.readByte();

            // 如果可读长度小于body长度，恢复读指针，退出。
            if (in.readableBytes() < length) {
                in.resetReaderIndex();
                return;
            }

            // 读取body
            ByteBuf bodyByteBuf = in.readBytes(length);

            byte[] array;
            int offset;

            int readableLen = bodyByteBuf.readableBytes();
            if (bodyByteBuf.hasArray()) {
                array = bodyByteBuf.array();
                offset = bodyByteBuf.arrayOffset() + bodyByteBuf.readerIndex();
            } else {
                array = new byte[readableLen];
                bodyByteBuf.getBytes(bodyByteBuf.readerIndex(), array, 0, readableLen);
                offset = 0;
            }

            //反序列化
            MessageLite result = decodeBody(dataType, array, offset, readableLen);
            out.add(result);
        }
        LOGGER.debug(LogCharGraph.HORIZONTAL_LINE);

    }

    /**
     * 解码消息体
     *
     * @param dataType
     * @param array
     * @param offset
     * @param length
     *
     * @return
     *
     * @throws Exception
     */
    public MessageLite decodeBody (byte dataType, byte[] array, int offset, int length) throws Exception {

        Parser parser = null;
        Class<?> msgClass = null;
        try {
            ProtobufXmlConfig protobufXmlConfig = PacketHelper.getProtobufXmlConfig();
            //找到消息类型
            msgClass = protobufXmlConfig.findClass(dataType);

            parser = this.reflectFindParser(msgClass);
        } catch (Exception e) {
            LOGGER.error("解码：解析消息类型报头失败:", e);
        }

        MessageLite messageLite = null;
        if (parser != null) {
            messageLite = (MessageLite) parser.parseFrom(array, offset, length);
            LOGGER.debug("|" + LogCharGraph.ARROWS + "解码：解析消息类型报头：" + dataType);
            LOGGER.debug("|" + LogCharGraph.ARROWS + "解码：消息类型      ：" + msgClass.getName());
        }

        return messageLite;
    }

    /**
     * 反射找到解析器
     *
     * @param msgClass
     *
     * @return
     *
     * @throws Exception
     */
    private Parser reflectFindParser (Class<?> msgClass) throws Exception {
        //反射调用getDefaultInstance()方法
        Method getDefaultInstanceMethod = msgClass.getDeclaredMethod("getDefaultInstance", null);
        getDefaultInstanceMethod.setAccessible(true);
        Object res = getDefaultInstanceMethod.invoke(null, null);


        //反射调用getParserForType()方法
        Method getParserForType = msgClass.getDeclaredMethod("getParserForType", null);
        getParserForType.setAccessible(true);

        return (Parser) getParserForType.invoke(res, null);
    }


}
