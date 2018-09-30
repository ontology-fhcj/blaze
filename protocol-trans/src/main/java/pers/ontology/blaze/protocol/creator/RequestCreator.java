package pers.ontology.blaze.protocol.creator;

import com.google.protobuf.Any;
import com.google.protobuf.Message;
import pers.ontology.blaze.protocol.TransportProtocol;
import pers.ontology.blaze.protocol.TransportProtocol.Request;

/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
public class RequestCreator extends BaseCreator<Request.Builder, Request> {


    public RequestCreator () {
        super(Request.newBuilder());
    }

    /**
     * 设置请求体
     *
     * @param body
     *
     * @return
     */
    public RequestCreator setBody (Message body) {
        Request.Builder cast = cast();
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
    public RequestCreator setHeader (TransportProtocol.Header header) {
        Request.Builder cast = cast();
        cast.setHeader(header);
        return this;
    }


    /**
     * 转换
     *
     * @return
     */
    @Override
    protected Request.Builder cast () {
        return (Request.Builder) builder;
    }

    public static RequestCreator get(){
        return new RequestCreator();
    }
}
