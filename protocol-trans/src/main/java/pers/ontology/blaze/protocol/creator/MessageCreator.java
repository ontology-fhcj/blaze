package pers.ontology.blaze.protocol.creator;


import pers.ontology.blaze.protocol.utils.MessageIdHelper;

import static pers.ontology.blaze.protocol.ChatProtocol.Message;

/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
public class MessageCreator extends BaseCreator<Message.Builder, Message> {

    public MessageCreator () {
        super(Message.newBuilder());
    }


    /**
     * @param body
     *
     * @return
     */
    public MessageCreator setBody (String body) {
        Message.Builder cast = cast();
        cast.setBody(body);
        return this;
    }

    /**
     * 来自
     *
     * @param from
     *
     * @return
     */
    public MessageCreator setFrom (String from) {
        Message.Builder cast = cast();
        cast.setFrom(from);
        return this;
    }

    /**
     * 目标
     *
     * @param to
     *
     * @return
     */
    public MessageCreator setTo (String to) {
        Message.Builder cast = cast();
        cast.setTo(to);
        return this;
    }


    @Override
    public Message done () {
        Message.Builder cast = cast();
        //
        cast.setId(MessageIdHelper.createId(cast));
        return (Message) builder.build();
    }



    /**
     * 转换
     *
     * @return
     */
    @Override
    protected Message.Builder cast () {
        return (Message.Builder) builder;
    }

    public static MessageCreator get () {
        return new MessageCreator();
    }
}
