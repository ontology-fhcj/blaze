package pers.ontology.blaze.server.server;

import io.netty.channel.ChannelHandlerContext;
import pers.ontology.blaze.packet.handler.DispatchChannelHandler;

import java.net.InetAddress;

/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
public class ServerHandler extends DispatchChannelHandler {


    /**
     * Handler本身被添加到ChannelPipeline时调用
     *
     * @param ctx
     *
     * @throws Exception
     */
    @Override
    public void handlerAdded (ChannelHandlerContext ctx) throws Exception {
    }

    /**
     * Handler本身被从ChannelPipeline中删除时调用
     *
     * @param ctx
     *
     * @throws Exception
     */
    @Override
    public void handlerRemoved (ChannelHandlerContext ctx) throws Exception {
    }

    /*
     *
     * 覆盖 channelActive 方法 在channel被启用的时候触发 (在建立连接的时候)
     *
     * channelActive 和 channelInActive 在后面的内容中讲述，这里先不做详细的描述
     * */
    @Override
    public void channelActive (ChannelHandlerContext ctx) throws Exception {

        System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");

        super.channelActive(ctx);
    }


}
