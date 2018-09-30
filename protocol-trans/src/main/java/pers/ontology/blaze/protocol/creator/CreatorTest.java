package pers.ontology.blaze.protocol.creator;

import pers.ontology.blaze.protocol.ChatProtocol;
import pers.ontology.blaze.protocol.TransportProtocol;

/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
public class CreatorTest {

    public static void main (String[] args) {
        TransportProtocol.Ack ack = AckCreator.get().setType(TransportProtocol.Ack.Type.CONFIRM).setTimestamp().done();

        System.out.println(ack);
        ChatProtocol.Message message = MessageCreator.get().setFrom("zhang").setTo("fuHuang").setBody("ss").done();

        System.out.println(message);

        ChatProtocol.Presence fuHuang = PresenceCreator.get()
                .setUid("zhang")
                .setAction(ChatProtocol.Presence.Action.LOGIN)
                .done();

        System.out.println(fuHuang);
    }
}
