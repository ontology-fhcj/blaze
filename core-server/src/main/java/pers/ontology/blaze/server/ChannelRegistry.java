package pers.ontology.blaze.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.ontology.blaze.server.handler.PresencePacketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
public class ChannelRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelRegistry.class);


    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    private Map<String, Channel> channelMap;

    public ChannelRegistry () {
        this.channelMap = new ConcurrentHashMap<>();
    }

    /**
     * 注册通道
     *
     * @param uid
     * @param channel
     */
    public void addChannel (String uid, Channel channel) {
        channels.add(channel);
        channelMap.put(uid, channel);
        LOGGER.info(uid + "的通道加入通道组");
    }


    /**
     * 删除通道
     *
     * @param uid 用户Id
     */
    public void removeChannel (String uid) {
        Channel channel = channelMap.get(uid);

        if (channel != null) {
            channel = channels.find(channel.id());
            if (channel.isActive()) {
                channel.closeFuture().addListener(future -> {
                    channelMap.remove(uid);
                    LOGGER.info(uid + "的通道成功移除通道组，并已关闭");
                });
            }
        }

    }

    public Channel findChannel (String uid) {
        return channelMap.get(uid);
    }

    /**
     * 关闭所有通道
     *
     * @return
     */
    public void closeAll () {
        channels.close().awaitUninterruptibly();
    }

}
