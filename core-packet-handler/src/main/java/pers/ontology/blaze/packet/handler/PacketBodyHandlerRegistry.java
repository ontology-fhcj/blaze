package pers.ontology.blaze.packet.handler;

import org.reflections.Reflections;
import pers.ontology.blaze.utils.annotation.CannotInstantiate;
import pers.ontology.blaze.utils.tool.ReflectUtils;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

import java.lang.annotation.Annotation;
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

                //忽略不能实例化的
                if (ReflectUtils.cannotInstantiate(packetBodyHandlerClass)) {
                    continue;
                }
                ParameterizedType pt = this.getParameterizedType(packetBodyHandlerClass);

                //实际参数类型
                Type actualTypeArgument = pt.getActualTypeArguments()[0];
                //忽略
                if (actualTypeArgument instanceof TypeVariableImpl) {
                    continue;
                }

                Class modelClass = (Class<?>) actualTypeArgument;

                if (modelClass == messageClass) {
                    PacketBodyHandler typeHandler = (PacketBodyHandler) packetBodyHandlerClass.newInstance();
                    messageTypeHandlerMapping.put(messageClass, typeHandler);
                }

            }
        }

        return messageTypeHandlerMapping.get(messageClass);

    }

    /**
     * @param packetBodyHandlerClass
     *
     * @return
     */
    private ParameterizedType getParameterizedType (Class packetBodyHandlerClass) {
        ParameterizedType pt = null;

        Type genericSuperclass = packetBodyHandlerClass.getGenericSuperclass();

        //extends
        if (genericSuperclass != null && genericSuperclass != Object.class) {
            if (genericSuperclass instanceof ParameterizedType) {
                pt = (ParameterizedType) genericSuperclass;
            }

        }
        //implements
        else {
            Type[] genericInterfaces = packetBodyHandlerClass.getGenericInterfaces();
            if (genericInterfaces[0] instanceof ParameterizedType) {
                pt = (ParameterizedType) genericInterfaces[0];
            }
        }
        return pt;
    }

}
