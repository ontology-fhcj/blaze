package pers.ontology.blaze.utils.tool;

import pers.ontology.blaze.utils.annotation.CannotInstantiate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * <h3>反射工具类</h3>
 */
public class ReflectUtils {

    private static final String VOID = "void";

    public static final class Modifier {
        public static final String ABSTRACT     = "abstract";
        public static final String DEFAULT      = "default";
        public static final String FINAL        = "final";
        public static final String NATIVE       = "native";
        public static final String PRIVATE      = "private";
        public static final String PROTECTED    = "protected";
        public static final String PUBLIC       = "public";
        public static final String STATIC       = "static";
        public static final String TRANSIENT    = "transient";
        public static final String VOLATILE     = "volatile";
        public static final String SYNCHRONIZED = "synchronized";
    }

    /**
     * 判断方法时候没有返回值
     *
     * @param method
     *
     * @return
     */
    public static boolean isEmptyReturn (Method method) {
        boolean is = true;
        Class<?> returnType = method.getReturnType();
        if (!VOID.equals(returnType.getName())) {
            is = false;
        }
        return is;
    }

    /**
     * 通过类的字节码获取实例
     *
     * @param clazz 字节码
     *
     * @return 实例
     *
     * @throws Exception 顶层异常
     */
    @SuppressWarnings("unchecked")
    public static <T> Object getInstance (Class<?> clazz) throws Exception {
        Object instance = null;
        if (clazz.isEnum()) {
            T[] enumConstants = (T[]) clazz.getEnumConstants();
            for (T t : enumConstants) {
                instance = t;
                break;
            }
        } else {
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            for (Constructor<?> constructor : constructors) {
                // 构造器私有
                if ((constructor.getModifiers() & 2) != 0) {
                    instance = clazz;
                    break;
                } else {
                    instance = clazz.newInstance();
                }
                break;

            }
        }
        return instance;
    }


    /**
     * 不能实例化对象吗
     *
     * @param clazz
     *
     * @return
     */
    public static boolean cannotInstantiate (Class<?> clazz) {
        Annotation annotation = clazz.getAnnotation(CannotInstantiate.class);
        return annotation != null;
    }


}