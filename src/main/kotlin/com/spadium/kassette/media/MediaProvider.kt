package com.spadium.kassette.media

abstract class MediaProvider {
    abstract fun getServiceName(): String

    abstract fun init()

    abstract fun destroy()
}