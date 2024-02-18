package com.wzmtr.eam.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

/**
 * Author: Li.Wang
 * Date: 2023/8/4 10:12
 */
public class StreamUtils {
    /**
     * 将集合按特定的键转换成map，满足一对一关系，若key重复则后者覆盖前者
     *
     * @param <K>       Map的key类型
     * @param <V>       Map的value类型
     * @param source    对象集合
     * @param keyMapper 值->key映射
     * @return map
     */
    public static <K, V> Map<K, V> toMap(Collection<V> source,
                                         Function<V, K> keyMapper) {
        return source.stream().collect(Collectors.toMap(keyMapper, Function.identity(), (key1, key2) -> key2));
    }

    /**
     * 从流中抽取<V>，生成集合List<V>.
     *
     * @return List<V>.
     */
    public static <E, V> List<V> mapToList(Collection<E> col, Function<E, V> func) {
        return col.stream()
                .map(func)
                .collect(Collectors.toList());
    }

    /**
     * 从集合流中抽取<R>，生成去重集合Set<R>.
     *
     * @param func 传入的函数.
     * @return Set<R>.
     */
    public static <E, V> Set<V> mapToSet(Collection<E> col, Function<E, V> func) {
        return col.stream()
                .map(func)
                .collect(Collectors.toSet());
    }


    /**
     * 从流中抽取<R>，根据R的不同值生成Map<R,List<T>>.
     */
    public static <E, R> Map<R, List<E>> groupBy(Collection<E> col, Function<E, R> func) {
        return col.stream().collect(groupingBy(func));
    }


    /**
     * 从流中抽取满足条件函数predicate的元素，并返回元素数量.
     *
     * @param predicate 条件函数.
     * @param <T>       函数的入参.
     * @return List<T>.
     */
    public static <T> long count(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream()
                .filter(predicate)
                .count();
    }



    /**
     * 流中的元素按比较函数排序，选出最大的元素。无，则手动返回.
     *
     * @param stream     流.
     * @param comparator 比较函数.
     * @param obj        当没有找到最大元素的返回值.
     * @param <T>        函数的入参.
     * @return List<T>.
     */
    public static <T> T max(Stream<T> stream, Comparator<T> comparator, T obj) {
        return stream
                .max(comparator)
                .orElse(obj);
    }


    public static <F, T> List<T> map(Collection<F> from, Function<? super F, T> mapper) {
        return CollectionUtil.isEmpty(from) ? Lists.newArrayList() : from.stream().map(mapper).collect(Collectors.toList());
    }

    public static <F, T> List<T> map(Collection<F> from, Predicate<F> predicate, Function<? super F, T> mapper) {
        return CollectionUtil.isEmpty(from) ? Lists.newArrayList() : from.stream().filter(predicate).map(mapper).collect(Collectors.toList());
    }

    public static <F, T> List<T> map(Stream<F> from, Function<? super F, T> mapper) {
        return from.map(mapper).collect(Collectors.toList());
    }


    public static <F, T> Set<T> mapToSet(Collection<F> from, Predicate<F> predicate, Function<? super F, T> mapper) {
        return from.stream().filter(predicate).map(mapper).collect(Collectors.toSet());
    }
    public static <F, T> List<T> mapToList(Collection<F> from, Predicate<F> predicate, Function<? super F, T> mapper) {
        return from.stream().filter(predicate).map(mapper).collect(Collectors.toList());
    }

}
