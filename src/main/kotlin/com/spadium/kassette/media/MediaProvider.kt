package com.spadium.kassette.media

abstract class MediaProvider {
    abstract fun getServiceName(): String

    abstract suspend fun init()

    abstract fun destroy()

    abstract fun getMedia(): MediaInfo
}