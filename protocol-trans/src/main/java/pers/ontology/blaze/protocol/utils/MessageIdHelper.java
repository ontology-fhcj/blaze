package pers.ontology.blaze.protocol.utils;

import pers.ontology.blaze.protocol.ChatProtocol;
import pers.ontology.blaze.utils.id.SnowFlake;

/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
public class MessageIdHelper {

    /**
     * 生成id
     * <p>  247058835670437888-from:name-to:name
     *
     * @param cast
     *
     * @return
     */
    public static String createId (ChatProtocol.Message.Builder cast) {
        //生成唯一标识
        long id = SnowFlake.getInstance().nextId();

        StringBuilder sb = new StringBuilder(String.valueOf(id));
        sb.append("-").append("from:").append(cast.getFrom()).append("-").append("to:").append(cast.getTo());
        return sb.toString();
    }


    /**
     * 解析消息Id
     *
     * @param messageId
     *
     * @return
     */
    public static MessageIdSegment parse (String messageId) {
        MessageIdSegment msgIdSegment = new MessageIdSegment();
        String[] segment = messageId.split("-");

        String id = segment[0];
        String fromSeg = segment[1];
        String toSeg = segment[2];

        String from = fromSeg.split(":")[1];
        String to = toSeg.split(":")[1];

        msgIdSegment.setUnique(id);
        msgIdSegment.setFrom(from);
        msgIdSegment.setTo(to);

        return msgIdSegment;
    }


}
