package pers.ontology.blaze.protocol.creator;

import com.google.protobuf.GeneratedMessageV3;

/**
 * <h3>基本构建器</h3>
 *
 * <p>封装公共构建逻辑
 *
 * @author ontology
 * @since 1.8
 */
public abstract class BaseCreator<T extends GeneratedMessageV3.Builder<T>, E> {

    protected com.google.protobuf.GeneratedMessageV3.Builder<T> builder;

    public BaseCreator (GeneratedMessageV3.Builder<T> builder) {
        this.builder = builder;
    }

    /**
     * 构建完成
     *
     * @return
     */
    public E done () {
        return (E) builder.build();
    }


    /**
     * 转换
     *
     * @return
     */
    protected abstract T cast ();

}
