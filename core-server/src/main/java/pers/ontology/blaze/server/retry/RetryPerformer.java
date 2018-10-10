package pers.ontology.blaze.server.retry;

import com.google.protobuf.MessageLite;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.ontology.blaze.server.handler.AckPacketHandler;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <h3>重试器</h3>
 *
 * <p>
 *
 * @author ontology
 * @since 1.8
 */
public class RetryPerformer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryPerformer.class);

    //消息对象
    private MessageLite   msg;
    //通道
    private Channel       channel;
    //是否重试成功
    private AtomicBoolean isDone;


    public RetryPerformer (MessageLite msg, Channel channel) {
        this.msg = msg;
        this.channel = channel;
        this.isDone = new AtomicBoolean(true);
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run () {
        while (isDone.get()) {
            try {
                Thread.sleep(1000);
                //异步转同步
                System.out.println(channel.isActive());
                System.out.println(channel.isWritable());
                System.out.println(channel.isOpen());
                ChannelFuture await = channel.writeAndFlush(msg).await();
                await.addListener(future -> {
                    if (future.cause() == null) {
                        isDone.set(false);
                    } else {
                        LOGGER.warn("重试发送消息失败，1秒后再次重试...");
                        LOGGER.warn(":", future.cause());
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
