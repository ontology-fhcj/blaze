package pers.ontology.blaze.packet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.ontology.blaze.packet.xml.ProtobufXmlConfig;
import pers.ontology.blaze.utils.tool.XmlUtils;

import java.io.InputStream;

/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
public class PacketHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketHelper.class);

    private static final String FILE_PATH = "classpath:proto/packet-type-mapping.xml";

    private static ProtobufXmlConfig protobufXmlConfig;


    /**
     * 初始化
     */
    public static void initProtobufXmlConfig () {

        LOGGER.info("Start parsing packet-type-mapping.xml");

        InputStream is = PacketHelper.class.getClassLoader().getResourceAsStream("proto/packet-type-mapping.xml");

        if (is != null) {
            try {
                protobufXmlConfig = XmlUtils.toObject(ProtobufXmlConfig.class, is);
            } catch (Exception e) {
                LOGGER.error("{} 解析错误！", FILE_PATH);
            }
        }

    }

    public static ProtobufXmlConfig getProtobufXmlConfig () {
        return protobufXmlConfig;
    }

}
