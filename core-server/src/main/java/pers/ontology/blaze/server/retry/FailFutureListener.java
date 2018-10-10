package pers.ontology.blaze.server.retry;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
public class FailFutureListener extends Thread implements GenericFutureListener<Future<? super Void>> {


    public FailFutureListener (RetryPerformer retryPerformer) {
        super(retryPerformer);
    }


    @Override
    public void operationComplete (Future future) throws Exception {
        Throwable cause = future.cause();
        //错误原因不为空说明消息发送失败,需要启动线程进行重发
        if (cause != null) {
            this.start();
        }
    }

}
