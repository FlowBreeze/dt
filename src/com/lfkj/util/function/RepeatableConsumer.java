package com.lfkj.util.function;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 对 java.util.function.Consumer 进行扩展
 */
@FunctionalInterface
public interface RepeatableConsumer<T> extends Consumer<T> {
    /**
     * 重复执行一个相同方法 每次将args中的一个对象作为参数传入
     *
     * @param args 多个传入参数，每有一个执行一次 @{{code}} accept 方法
     */
    default void acceptAll(T... args) {
        for (T arg : args) {
            accept(arg);
        }
    }

    /**
     * 通过迭代器的 hasNext 方法判断是否存在下一个元素，如果为真获取下个元素执行操作
     * 为假结束循环
     *
     * @param hasNext 该方法用于判断是否有下一个元素
     * @param next 返回下一个元素
     */
    default void acceptNextAll(BooleanSupplier hasNext, Supplier<T> next) {
        while (hasNext.getAsBoolean()) {
            accept(next.get());
        }
    }

    /**
     * 执行方法后返回该对象 以便下次调用
     *
     * @param arg 传入参数
     */
    default RepeatableConsumer<T> reAccept(T arg) {
        accept(arg);
        return this;
    }

}
