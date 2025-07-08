package com.spadium.kassette.media

abstract class MediaProvider {
    constructor() {
        this.init()
    }

    open var state: MediaManager.MediaState = MediaManager.MediaState.OTHER

    abstract val info: MediaInfo
        private set

    abstract fun getServiceName(): String
    @Deprecated(
        "Constructors will initialize providers since git commit 964028d3! This will be removed by release!",
        ReplaceWith("constructor()")
    )
    open fun init() {}

    abstract fun destroy()

    open fun getMedia(): MediaInfo { return info }

    open fun sendCommand(cmd: String, payload: Any): Int { return 0 }

    abstract suspend fun update()
}