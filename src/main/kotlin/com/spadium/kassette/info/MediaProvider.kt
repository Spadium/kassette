package com.spadium.kassette.info

abstract class MediaProvider {
    abstract fun getServiceName(): String

    abstract fun init()

    abstract fun destroy()
}