package me.spadium.kassette.config

import me.spadium.kassette.config.MainConfig.Companion.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Path
import kotlin.collections.forEach
import kotlin.io.path.writeBytes
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.createType
import kotlin.reflect.full.findAnnotation

abstract class Config<T> {
    abstract class ConfigCompanion<T> where T : Config<T> {
        abstract var Instance: T
        open val configUpdateListeners: MutableList<(KProperty<*>, T, T) -> Unit> = mutableListOf()

        fun addListener(listener: (KProperty<*>, T, T) -> Unit) {
            configUpdateListeners.add(listener)
        }

        abstract fun reload(): T

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
        val configPath: Path = FabricLoader.getInstance().configDir.resolve("kassette/")
        @JvmStatic
        var annotatedClassCache: MutableList<KClass<Config<*>>> = mutableListOf();

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

        @JvmStatic
        @Suppress("UNCHECKED_CAST", "SENSELESS_COMPARISON")
        fun reloadAll() {
            annotatedClassCache.forEach { clazz ->
                val companionObj = clazz.companionObjectInstance as? ConfigCompanion<Config<*>>
                    ?: error("Invalid class, somehow")
                println(companionObj::class.qualifiedName)
                if (clazz::objectInstance != null) {
                    companionObj.Instance = companionObj.reload()
                }

            }
        }

        @OptIn(ExperimentalSerializationApi::class)
        inline fun <reified T : Config<T>> load(): T {
            return load(T::class)
        }

        @OptIn(ExperimentalSerializationApi::class)
        inline fun <reified T: Config<T>> load(klazz: KClass<T>): T {
            var configOut: T = klazz.createInstance()
            if (T::class.java.isAnnotationPresent(ConfigMeta::class.java)) {
                val annotationMeta = T::class.java.getAnnotation(ConfigMeta::class.java)
                val configFile = configPath.resolve("${annotationMeta.type.path}${annotationMeta.configCategory}.json")
                try {
                    val jsonIn: T = json.decodeFromStream(configFile.toFile().inputStream())
                    configOut = jsonIn
                } catch (e: Exception) {

                }
            } else {
                throw Exception("Invalid class!")
            }
            return configOut
        }
    }

    abstract fun validate()

    abstract fun save()
}