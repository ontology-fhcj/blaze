package pers.ontology.blaze.packet.handler;

import org.reflections.Reflections;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h3>包体处理器注册表</h3>
 *
 * @author ontology
 * @since 1.8
 */
public class PacketBodyHandlerRegistry {

    private Map<Class<?>, PacketBodyHandler>        messageTypeHandlerMapping;
    //
    private Reflections                             reflections;
    private Set<Class<? extends PacketBodyHandler>> packetBodyHandlerSet;

    public PacketBodyHandlerRegistry () {
        this.messageTypeHandlerMapping = new ConcurrentHashMap<>();
        this.reflections = new Reflections();
        this.preload();
    }

    /**
     * 预先把PacketBodyHandler的实现类找到，反射性能比较低，所以不在runtime去调用
     */
    public void preload () {
        this.packetBodyHandlerSet = reflections.getSubTypesOf(PacketBodyHandler.class);
    }

    /**
     * 找到包处理器
     *
     * @param messageClass 消息类型
     *
     * @return
     *
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public PacketBodyHandler findPacketBodyHandler (Class<?> messageClass) throws IllegalAccessException,
                                                                                  InstantiationException {

        if (!messageTypeHandlerMapping.containsKey(messageClass)) {
            //FIXME:考虑先缓存起来
            for (Class packetBodyHandlerClass : packetBodyHandlerSet) {

                Type[] genericInterfaces = packetBodyHandlerClass.getGenericInterfaces();

                ParameterizedType pt = (ParameterizedType) genericInterfaces[0];
                Class modelClass = (Class<?>) pt.getActualTypeArguments()[0];


                if (modelClass == messageClass) {
                    PacketBodyHandler typeHandler = (PacketBodyHandler) packetBodyHandlerClass.newInstance();
                    messageTypeHandlerMapping.put(messageClass, typeHandler);
                }

            }
        }

        return messageTypeHandlerMapping.get(messageClass);

    }

}
