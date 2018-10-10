package pers.ontology.blaze.protocol.utils;

/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
public class MessageIdSegment {

    private String unique;

    private String from;

    private String to;

    public String getUnique () {
        return unique;
    }

    public void setUnique (String unique) {
        this.unique = unique;
    }

    public String getFrom () {
        return from;
    }

    public void setFrom (String from) {
        this.from = from;
    }

    public String getTo () {
        return to;
    }

    public void setTo (String to) {
        this.to = to;
    }
}
