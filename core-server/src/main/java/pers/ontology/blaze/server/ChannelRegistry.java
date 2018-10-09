package pers.ontology.blaze.server;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h3>通道注册表</h3>
 *
 * <p>本类作用在于将Channel对象缓存起来，保持用户在线的状态，如果用户下线，则会移除相关Channel。
 *
 * @author ontology
 * @since 1.8
 */
public class ChannelRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelRegistry.class);


    private ChannelGroup         channels;
    private Map<String, Channel> channelMap;


    public ChannelRegistry () {
        this.channelMap = new ConcurrentHashMap<>();
        this.channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    }

    /**
     * 注册通道
     *
     * @param uid     用户标识
     * @param channel 通道对象
     */
    public void addChannel (String uid, Channel channel) {
        channels.add(channel);
        channelMap.put(uid, channel);
        LOGGER.info(uid + "的通道加入通道组");
    }


    /**
     * 删除通道
     *
     * @param uid 用户标识
     */
    public void removeChannel (String uid) {
        Channel channel = channelMap.get(uid);

        if (channel != null) {
            channel = channels.find(channel.id());
            if (channel.isActive()) {
                //关闭并添加钩子
                channel.close().addListener(new BaseCloseListener(uid));
            }
        }

    }

    /**
     * 基本的关闭监听处理
     */
    class BaseCloseListener implements GenericFutureListener<Future<? super Void>> {

        private String uid;

        BaseCloseListener (String uid) {
            this.uid = uid;
        }

        /**
         * Invoked when the operation associated with the {@link Future} has been completed.
         *
         * @param future the source {@link Future} which called this callback
         */
        @Override
        public void operationComplete (Future future) throws Exception {
            Throwable cause = future.cause();
            //成功关闭
            if (cause == null) {
                channelMap.remove(uid);
                LOGGER.info(uid + "通道成功移除通道组，并已关闭");
            }
            //关闭失败
            else {
                LOGGER.info(uid + "通道关闭失败：", cause);
            }
        }
    }

    /**
     * 查找通道
     *
     * @param uid 用户标识
     *
     * @return
     */
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
