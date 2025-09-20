package com.spadium.kassette.config

import kotlin.reflect.KProperty

abstract class Config<T> {
    abstract class ConfigCompanion<T> where T : Config<T>{
        abstract var Instance: T
        open val configUpdateListeners: MutableList<(KProperty<*>, T, T) -> Unit> = mutableListOf()

        fun addListener(listener: (KProperty<*>, T, T) -> Unit) {
            configUpdateListeners.add(listener)
        }

        protected fun yellAtListeners(property: KProperty<*>, oldValue: T, newValue: T) {
            val invalidListeners: MutableList<(KProperty<*>, T, T) -> Unit> = mutableListOf()
            configUpdateListeners.forEach {
                try {
                    it.invoke(property, oldValue, newValue)
                } catch (t: Throwable) {
                    invalidListeners.add(it)
                }
            }
            if (invalidListeners.isNotEmpty()) {
                invalidListeners.forEach {
                    configUpdateListeners.remove(it)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun checkColorArray(arr: IntArray, minimumSize: Int, maximumSize: Int, defaultValue: Int): IntArray {
            val list = arr.toMutableList()
            if (arr.size in minimumSize..maximumSize && (maximumSize - minimumSize) == 1) {
                if (arr.size == maximumSize) {
                    return arr
                } else if (arr.size == minimumSize) {
                    list.addLast(defaultValue)
                }
            } else {
                throw RuntimeException("Kassette Config: Color array too small/too big!")
            }
            return list.toIntArray()
        }
    }

    abstract fun validate()
}