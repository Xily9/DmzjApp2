package com.xily.dmzj2.utils

/**
 * Created by Xily on 2020/2/6.
 */
fun <T> Class<*>.invokeMethod(name: String, vararg params: Any): T? {
    var clz: Class<*> = this
    val argList = arrayListOf<Class<*>>()
    params.forEach {
        argList.add(it::class.java)
    }
    while (clz != Any::class.java) {
        try {
            val method = clz.getDeclaredMethod(name, *argList.toArray(arrayOf<Class<*>>()))
            method.isAccessible = true
            return method.invoke(null, *params) as T
        } catch (e: Exception) {
            clz = clz.superclass!!
        }
    }
    return null
}

fun <T> Any.invokeMethod(name: String, vararg params: Any): T? {
    var clz: Class<*> = this::class.java
    val argList = arrayListOf<Class<*>>()
    params.forEach {
        argList.add(it::class.java)
    }
    while (clz != Any::class.java) {
        try {
            val method = clz.getDeclaredMethod(name, *argList.toArray(arrayOf<Class<*>>()))
            method.isAccessible = true
            return method.invoke(this, *params) as T
        } catch (e: Exception) {
            clz = clz.superclass!!
        }
    }
    return null
}

fun <T> Class<*>.getField(name: String): T? {
    var clz: Class<*> = this
    while (clz != Any::class.java) {
        try {
            val field = clz.getDeclaredField(name)
            field.isAccessible = true
            return field.get(null) as T?
        } catch (e: Exception) {
            clz = clz.superclass!!
        }
    }
    return null
}

fun <T> Any.getField(name: String): T? {
    var clz: Class<*> = this::class.java
    while (clz != Any::class.java) {
        try {
            val field = clz.getDeclaredField(name)
            field.isAccessible = true
            return field.get(this) as T?
        } catch (e: Exception) {
            clz = clz.superclass!!
        }
    }
    return null
}

fun Class<*>.setField(name: String, value: Any) {
    var clz: Class<*> = this
    while (clz != Any::class.java) {
        try {
            val field = clz.getDeclaredField(name)
            field.isAccessible = true
            return field.set(null, value)
        } catch (e: Exception) {
            e.printStackTrace()
            clz = clz.superclass!!
        }
    }
}

fun Any.setField(name: String, value: Any) {
    var clz: Class<*> = this::class.java
    while (clz != Any::class.java) {
        try {
            val field = clz.getDeclaredField(name)
            field.isAccessible = true
            return field.set(this, value)
        } catch (e: Exception) {
            e.printStackTrace()
            clz = clz.superclass!!
        }
    }
}

fun <T> Class<T>.newInstance(vararg params: Any): T? {
    val argList = arrayListOf<Class<*>>()
    params.forEach {
        argList.add(it::class.java)
    }
    try {
        val constructor = getConstructor(*argList.toArray(arrayOf<Class<*>>()))
        return constructor.newInstance(*params)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}