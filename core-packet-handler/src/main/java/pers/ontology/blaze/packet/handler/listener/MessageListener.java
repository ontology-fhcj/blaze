package pers.ontology.blaze.packet.handler.listener;


/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
public interface MessageListener<T> {

    /**
     * @param msg
     */
    void fire (T msg);
}
