package com.lfkj.util;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;

public class Context {
    /**
     * 获取指定值,当值为空时,使用默认值代替代替
     *
     * @param obj        需要获取的值
     * @param defaultVal 用以代替的默认值
     * @param <T>        获取值的类型
     */
    public static <T> T getOrDefault(@Nullable T obj, T defaultVal) {
        return nonNull(obj) ? obj : defaultVal;
    }


    /**
     * 获取指定值,当值为空时,执行方法生成一个新的值
     *
     * @param obj        需要获取的值
     * @param defaultVal 用以代替的生成默认值的方法
     * @param <T>        获取值的类型
     */
    public static <T> T getOrDefault(@Nullable T obj, Supplier<T> defaultVal) {
        return nonNull(obj) ? obj : defaultVal.get();
    }

    /**
     * 当对象不为空时,执行操作
     *
     * @param obj        进行操作的对象,可以为空
     * @param thenAccept 函数接口,对对象进行的后续操作
     * @param <T>        指定对象的类型
     */
    public static <T> void nullOrThen(@Nullable T obj, Consumer<T> thenAccept) {
        if (nonNull(obj))
            thenAccept.accept(obj);
    }

    /**
     * 当对象不为空时,执行操作并返回结果
     *
     * @param obj       进行操作的对象,可以为空
     * @param thenApply 函数接口,对对象进行的后续操作
     * @param <T>       指定对象的类型
     * @param <R>       thenApply 返回的类型
     */
    @Nullable
    public static <T, R> R nullOrThen(@Nullable T obj, Function<T, R> thenApply) {
        return nonNull(obj) ? thenApply.apply(obj) : null;
    }

    /**
     * 根据所要赋值的参数类型进行隐式类型转换
     *
     * @param o   原变量
     * @param <T> 返回值类型，根据上下文判断
     * @return T 类型的 o
     * @throws ClassCastException 类型无法转换时抛出异常
     */
    public static <T> T cast(Object o) throws ClassCastException {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        return t;
    }

    public static <T> T cast(Object o, Class<T> toClass) throws ClassCastException {
        @SuppressWarnings("unchecked")
        T t = toClass.cast(o);
        return t;
    }


    public static <T> T TODO() {
        return TODO("");
    }

    public static <T> T TODO(String disc) {
        throw new UnsupportedOperationException();
    }
}