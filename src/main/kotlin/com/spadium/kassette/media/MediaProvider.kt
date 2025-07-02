package com.spadium.kassette.media

interface MediaProvider {
    fun getServiceName(): String

    fun init()

    fun destroy()

    fun getMedia(): MediaInfo

    fun update()
}