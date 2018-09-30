package pers.ontology.blaze.protocol.creator;


import static pers.ontology.blaze.protocol.ChatProtocol.Presence;

/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
public class PresenceCreator extends BaseCreator<Presence.Builder, Presence> {

    public PresenceCreator () {
        super(Presence.newBuilder());
    }

    /**
     * 用户Id
     *
     * @param uid
     *
     * @return
     */
    public PresenceCreator setUid (String uid) {
        Presence.Builder cast = cast();
        cast.setUid(uid);
        return this;
    }

    /**
     * @param action
     *
     * @return
     */
    public PresenceCreator setAction (Presence.Action action) {
        Presence.Builder cast = cast();
        cast.setAction(action);
        return this;
    }

    /**
     * 转换
     *
     * @return
     */
    @Override
    protected Presence.Builder cast () {
        return (Presence.Builder) builder;
    }


    public static PresenceCreator get () {
        return new PresenceCreator();
    }

}
