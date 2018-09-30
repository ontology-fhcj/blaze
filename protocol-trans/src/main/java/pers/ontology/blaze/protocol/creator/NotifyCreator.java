package pers.ontology.blaze.protocol.creator;

import com.google.protobuf.Any;
import com.google.protobuf.Message;
import pers.ontology.blaze.protocol.TransportProtocol;
import pers.ontology.blaze.protocol.TransportProtocol.Notify;

/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
public class NotifyCreator extends BaseCreator<Notify.Builder, Notify> {


    public NotifyCreator () {
        super(Notify.newBuilder());
    }

    /**
     * 设置请求体
     *
     * @param body
     *
     * @return
     */
    public NotifyCreator setBody (Message body) {
        Notify.Builder cast = cast();
        cast.setBody(Any.pack(body));
        return this;
    }

    /**
     * 设置请求头
     *
     * @param header
     *
     * @return
     */
    public NotifyCreator setHeader (TransportProtocol.Header header) {
        Notify.Builder cast = cast();
        cast.setHeader(header);
        return this;
    }


    /**
     * 转换
     *
     * @return
     */
    @Override
    protected Notify.Builder cast () {
        return (Notify.Builder) builder;
    }

    public static NotifyCreator get () {
        return new NotifyCreator();
    }
}
