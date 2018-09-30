package pers.ontology.blaze.packet.handler;

/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
public class HandlerContext {


    private static PacketBodyHandlerRegistry packetBodyHandlerRegistry;


    public static PacketBodyHandlerRegistry getPacketBodyHandlerRegistry () {
        return packetBodyHandlerRegistry;
    }

    public static void setPacketBodyHandlerRegistry (PacketBodyHandlerRegistry packetBodyHandlerRegistry) {
        HandlerContext.packetBodyHandlerRegistry = packetBodyHandlerRegistry;
    }
}
