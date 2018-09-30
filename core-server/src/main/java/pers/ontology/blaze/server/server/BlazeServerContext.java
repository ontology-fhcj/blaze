package pers.ontology.blaze.server.server;


import pers.ontology.blaze.server.ChannelRegistry;

/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
public class BlazeServerContext {


    private static ChannelRegistry channelRegistry;


    public static ChannelRegistry getChannelRegistry () {
        return channelRegistry;
    }

    public static void setChannelRegistry (ChannelRegistry channelRegistry) {
        BlazeServerContext.channelRegistry = channelRegistry;
    }
}
