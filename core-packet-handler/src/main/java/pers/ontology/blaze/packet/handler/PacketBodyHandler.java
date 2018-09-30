package pers.ontology.blaze.packet.handler;

import io.netty.channel.ChannelHandlerContext;

/**
 * <h3>包体处理器</h3>
 *
 * @author ontology
 * @since 1.8
 */
public interface PacketBodyHandler<T> {

    /**
     * 解析包体
     *
     * @param msg 数据
     * @param ctx 通道
     *
     * @return
     */
    Object parse (T msg, ChannelHandlerContext ctx) throws Exception;

}
