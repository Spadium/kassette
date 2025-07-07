package com.spadium.kassette.media

abstract class MediaProvider {
    constructor() {
        init()
    }

    open var state: MediaManager.MediaState = MediaManager.MediaState.OTHER

    abstract fun getServiceName(): String

    fun init() {}

    abstract fun destroy()

    abstract fun getMedia(): MediaInfo

    abstract suspend fun update()
}