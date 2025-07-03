package com.spadium.kassette.media

abstract class MediaProvider {
    open var state: MediaManager.MediaState = MediaManager.MediaState.OTHER

    abstract fun getServiceName(): String

    abstract fun init()

    abstract fun destroy()

    abstract fun getMedia(): MediaInfo

    abstract suspend fun update()
}