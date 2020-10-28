package com.asset.appwork.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

/**
 * Created by karim on 10/22/20.
 * @classdesc ReflectionUtil class used for handle reflection
 * @example <caption>Example usage of class.</caption>
 * // return execute System.out.println if value found
 * ReflectionUtil.of(identityComponentsPerson).ifPresent("getId", (s)-> {System.out.println(s);})
 */
public class ReflectionUtil<T> {
    private T obj;
    private T value;
    public static <V> ReflectionUtil of(V object) {
        return new ReflectionUtil(object);
    }

    private <V> ReflectionUtil(T object) {
        this.obj = object;
    }

    private void present(Consumer<? super T> var1) {
        if(this.value != null) {
            var1.accept(this.value);
        }
    }
    public boolean isPresent(){
        return value == null ? false : true;
    }

    public  ReflectionUtil ifPresent(String methodName, Consumer<? super T> consumer ) {
        this.value = invoke(methodName);

        if (this.value != null)  present(consumer);

        return this;
    }

    private T invoke(String methodName){
        T value = null;
        try {
            Method method = this.obj.getClass().getMethod(methodName);
            value = (T) method.invoke(obj);
        } catch (NoSuchMethodException e) {
            value = null;
        } catch (InvocationTargetException e) {
            value = null;
        } catch (IllegalAccessException e) {
            value = null;
        }
        return value;
    }
}
