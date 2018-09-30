package pers.ontology.blaze.utils.tool;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * <h3>反射工具类</h3>
 *
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
     * 获取字段
     *
     * @param clazz
     * @param fieldName
     *
     * @return
     */
    public static Field getDeclaredField (Class<?> clazz, String fieldName) {
        Field field = null;

        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException e1) {
        } catch (SecurityException e1) {
            e1.printStackTrace();
        }
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (Exception e) {
                // 如果这里的异常打印或者往外抛，则就不会执行clazz =
                // clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }

        return null;
    }

    /**
     * 设置字段值
     *
     * @param propertyName 字段名
     * @param obj          实例对象
     * @param value        新的字段值
     *
     * @throws SecurityException        安全验证异常
     * @throws NoSuchFieldException     字段未找到异常
     * @throws IllegalAccessException   非法访问异常
     * @throws IllegalArgumentException 非法参数异常
     */
    public static void setDeclaredFieldValue (Object obj, String propertyName, Object value) throws
                                                                                             NoSuchFieldException,
                                                                                             SecurityException,
                                                                                             IllegalArgumentException,
                                                                                             IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(propertyName);
        if (field != null) {
            field.setAccessible(true);
            field.set(obj, value);
        }
    }

    /**
     * 获取字段值
     *
     * @param obj          实例对象
     * @param propertyName 　属性名
     *
     * @return 值
     */
    public static Object getDeclaredFieldValue (Object obj, String propertyName) throws NoSuchFieldException,
                                                                                        IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(propertyName);
        if (field != null) {
            field.setAccessible(true);
            return field.get(obj);
        }
        return null;

    }

    /**
     * 获取字段列表
     *
     * @param obj 实例对象
     *
     * @return 字段列表
     */
    public static Field[] listDeclaredField (Object obj) {
        return obj.getClass().getDeclaredFields();
    }

    /**
     * 验证字段是否存在
     *
     * @param clazz        字节码
     * @param propertyName 字段名
     *
     * @return 是否存在
     *
     * @throws SecurityException 安全验证异常
     */
    public static boolean existField (Class<?> clazz, String propertyName) throws SecurityException {
        boolean is = false;
        Field field;
        try {
            field = clazz.getField(propertyName);
        } catch (NoSuchFieldException e) {
            // 未找到该字段
            return false;
        }
        if (field != null) {
            is = true;
        }
        return is;
    }

    /**
     * 通过字段值获取字段
     *
     * @param clazz 　字节码
     * @param value 　字段值
     *
     * @return 字段对象
     *
     * @throws Exception 顶层异常
     */
    public static Field getFieldByValue (Class<?> clazz, Object value) throws Exception {

        Object instance = getInstance(clazz);

        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            Object o = declaredField.get(instance);
            if (o.equals(value)) {
                return declaredField;
            }
        }

        return null;
    }

    /**
     * 获取字段
     *
     * @param clazz 字节码
     * @param name  字段名
     *
     * @return 字段对象
     *
     * @throws Exception 顶层异常
     */
    public static Field getField (Class<?> clazz, String name) throws Exception {
        return clazz.getDeclaredField(name);
    }

    /**
     * 获取字段值
     *
     * @param obj
     * @param name
     *
     * @return
     *
     * @throws Exception
     */
    public static Object getFieldValue (Object obj, String name) throws Exception {
        Field field = getField(obj.getClass(), name);
        field.setAccessible(true);
        return field.get(obj);
    }

    /**
     * 获取修饰符
     *
     * @param <T>
     *
     * @return
     */
    public static <T> String getModifiers (T t) {
        Member member;
        if (t instanceof Member) {
            member = (Member) t;
        } else {
            throw new ClassCastException(t + "不能转换为 java.lang.reflect.Member");
        }
        return java.lang.reflect.Modifier.toString(member.getModifiers());
    }

    /**
     * 所有实现类
     *
     * @param c
     *
     * @return
     */
    public static List<Class> subAllClass (Class c,String packageName) {

        List<Class> returnClassList = new ArrayList<>(); // 返回结果

        //        if (c.isInterface()) {

//        String packageName = c.getPackage().getName(); // 获得当前的包名
        try {
            List<Class> allClass = getClasses(packageName); // 获得当前包下以及子包下的所有类

            // 判断是否是同一个接口
            for (int i = 0; i < allClass.size(); i++) {

                if (c.isAssignableFrom(allClass.get(i))) { // 判断是不是一个接口
                    if (!c.equals(allClass.get(i))) { // 本身不加进去
                        returnClassList.add(allClass.get(i));
                    }

                }
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        //        }
        return returnClassList;
    }

    /**
     * 从一个包中查找出所有的类，在jar包中不能查找
     *
     * @param packageName
     *
     * @return
     *
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private static List<Class> getClasses (String packageName) throws ClassNotFoundException, IOException {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration resources = classLoader.getResources(path);

        List<File> dirs = new ArrayList<>();

        while (resources.hasMoreElements()) {
            URL resource = (URL) resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }

        ArrayList<Class> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;

    }

    /**
     * @param directory
     * @param packageName
     *
     * @return
     *
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses (File directory, String packageName)

            throws ClassNotFoundException {

        List<Class> classes = new ArrayList<>();

        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(
                        Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));

            }
        }
        return classes;

    }


}