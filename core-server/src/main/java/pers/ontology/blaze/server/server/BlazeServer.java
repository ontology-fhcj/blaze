package pers.ontology.blaze.server.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.ontology.blaze.packet.PacketHelper;
import pers.ontology.blaze.packet.handler.HandlerContext;
import pers.ontology.blaze.packet.handler.PacketBodyHandlerRegistry;
import pers.ontology.blaze.server.ChannelRegistry;

/**
 * <h3>服务器</h3>
 *
 * @author ontology
 * @since 1.8
 */
public class BlazeServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlazeServer.class);

    /**
     * 服务端监听的端口地址
     */
    private static final int PORT = 7878;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private ServerBootstrap bootstrap;

    public BlazeServer () {
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();

        this.init();
    }

    /**
     * 初始化
     */
    private void init () {
        LOGGER.info("IM服务器初始化开始...");
        //初始化配置
        LOGGER.info("初始化消息类型映射配置...");
        PacketHelper.initProtobufXmlConfig();


        //设置包体处理器注册表
        LOGGER.info("初始化包体处理器注册表...");
        PacketBodyHandlerRegistry packetBodyHandlerRegistry = new PacketBodyHandlerRegistry();
        HandlerContext.setPacketBodyHandlerRegistry(packetBodyHandlerRegistry);

        //设置通道注册表
        LOGGER.info("初始化通道注册表...");
        ChannelRegistry channelRegistry = new ChannelRegistry();
        BlazeServerContext.setChannelRegistry(channelRegistry);

        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);

    }


    public void start () throws InterruptedException {
        try {
            // 服务器绑定端口监听
            ChannelFuture f = bootstrap.bind(PORT).sync();

            //JVM关闭回调
            LOGGER.info("初始化JVM关闭回调...");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> stop()));


            LOGGER.info("IM服务器初始化完成！");
            // 监听服务器关闭监听
            f.channel().closeFuture().sync();

        } finally {
            stop();
        }
    }

    /**
     * 关闭
     */
    private void stop () {
        LOGGER.info("服务器将关闭...");
        //关闭所有通道
        BlazeServerContext.getChannelRegistry().closeAll();

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public void setChildHandler (ChannelHandler channelHandler) {
        bootstrap.childHandler(channelHandler);
    }
}
