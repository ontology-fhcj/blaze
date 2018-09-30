package pers.ontology.blaze.server.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import pers.ontology.blaze.packet.coder.MultiProtobufDecoder;
import pers.ontology.blaze.packet.coder.MultiProtobufEncoder;

/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

//    @Autowired
//    private MultiProtobufDecoder multiProtobufDecoder;
//    @Autowired
//    private MultiProtobufEncoder multiProtobufEncoder;
//    @Autowired
//    private ServerHandler serverHandler;

    /**
     * @param ch
     *
     * @throws Exception
     */
    @Override
    protected void initChannel (SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 解码 和 编码
        //        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        //        pipeline.addLast("decoder", new ProtobufDecoder(ProtocolPacket.O.getDefaultInstance()));
        //        pipeline.addLast("encoder",new ProtobufVarint32LengthFieldPrepender());
        //        pipeline.addLast(new ProtobufEncoder());

        pipeline.addLast("decoder", new MultiProtobufDecoder());
        pipeline.addLast("encoder", new MultiProtobufEncoder());
        //
        pipeline.addLast("handler", new ServerHandler());
    }


//    public void setMultiProtobufDecoder (MultiProtobufDecoder multiProtobufDecoder) {
//        this.multiProtobufDecoder = multiProtobufDecoder;
//    }
//
//    public void setMultiProtobufEncoder (MultiProtobufEncoder multiProtobufEncoder) {
//        this.multiProtobufEncoder = multiProtobufEncoder;
//    }
//
//    public void setServerHandler (ServerHandler serverHandler) {
//        this.serverHandler = serverHandler;
//    }
}
