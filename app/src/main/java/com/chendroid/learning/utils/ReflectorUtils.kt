package com.chendroid.learning.utils

import java.lang.reflect.Field

object ReflectorUtils {


    /**de
     * 通过反射获取某对象，并设置私有可访问
     *
     * @param any   该属性所属类的对象
     * @param clazz 该属性所属类
     * @param fieldName 属性名
     * @return 该属性对象
     */
    @Throws(NoSuchFieldException::class, IllegalAccessException::class, IllegalArgumentException::class)
    private fun getField(any: Any, clazz: Class<*>, fieldName: String): Any? {
        val localField: Field = clazz.getDeclaredField(fieldName)
        localField.isAccessible = true
        return localField.get(any)
    }

    /**
     * 给某属性赋值，并设置私有可访问
     *
     * @param obj   该属性所属类的对象
     * @param clazz 该属性所属类
     * @param value 值
     */
    @Throws(
        NoSuchFieldException::class,
        IllegalAccessException::class,
        IllegalArgumentException::class
    )
    fun setField(obj: Any?, clazz: Class<*>, value: Any?) {
        val localField = clazz.getDeclaredField("dexElements")
        localField.isAccessible = true
        localField[obj] = value
    }


    /**
     * 通过反射获取 BaseDexClassLoader 对象中的PathList对象
     */
    @Throws(
        NoSuchFieldException::class,
        IllegalAccessException::class,
        IllegalArgumentException::class,
        ClassNotFoundException::class
    )
    fun getPathList(baseDexClassLoader: Any): Any? {
        return getField(
            baseDexClassLoader,
            Class.forName("dalvik.system.BaseDexClassLoader"),
            "pathList"
        )
    }


    /**
     * 通过反射获取BaseDexClassLoader对象中的PathList对象，再获取dexElements对象
     *
     * @param paramObject PathList对象
     * @return dexElements对象
     */
    @Throws(
        NoSuchFieldException::class,
        IllegalAccessException::class,
        IllegalArgumentException::class
    )
    fun getDexElements(paramObject: Any): Any? {
        return getField(paramObject, paramObject.javaClass, "dexElements")
    }





}