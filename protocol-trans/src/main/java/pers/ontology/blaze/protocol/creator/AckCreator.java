package pers.ontology.blaze.protocol.creator;

import pers.ontology.blaze.protocol.TransportProtocol;
import pers.ontology.blaze.protocol.TransportProtocol.Ack;

/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
public class AckCreator extends BaseCreator<Ack.Builder, Ack> {


    public AckCreator () {
        super(Ack.newBuilder());
    }


    /**
     * 设置ack类型
     *
     * @param type
     *
     * @return
     */
    public AckCreator setType (TransportProtocol.Ack.Type type) {
        Ack.Builder ackBuilder = cast();
        ackBuilder.setType(type);
        return this;
    }

    public AckCreator setTimestamp () {
        Ack.Builder ackBuilder = cast();
        ackBuilder.setTimestamp(String.valueOf(System.currentTimeMillis()));
        return this;
    }
    /**
     * 转换
     *
     * @return
     */
    @Override
    protected Ack.Builder cast () {
        return (Ack.Builder) builder;
    }


    public static AckCreator get () {
        return new AckCreator();
    }
}
