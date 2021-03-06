package pers.ontology.blaze.packet.handler;

import org.apache.commons.collections4.CollectionUtils;
import pers.ontology.blaze.utils.annotation.CannotInstantiate;
import pers.ontology.blaze.packet.handler.listener.MessageListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
@CannotInstantiate
public abstract class AbstractPacketBodyHandler<T> implements PacketBodyHandler<T> {

    //存储所有监听
    private List<MessageListener<T>> messageListenerSet;
    private Lock                     lock;

    /**
     *
     */
    public AbstractPacketBodyHandler () {
        this.messageListenerSet = new ArrayList<>();
        this.lock = new ReentrantLock();
    }


    /**
     * 通知所有订阅者
     *
     * @param msg
     */
    public void notifyAny (T msg) {
        try {
            lock.lock();
            if (!CollectionUtils.isEmpty(messageListenerSet)) {
                messageListenerSet.forEach(o -> o.fire(msg));
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 订阅
     *
     * @param messageListener
     */
    public void subscribe (MessageListener<T> messageListener) {
        this.messageListenerSet.add(messageListener);
    }

}
