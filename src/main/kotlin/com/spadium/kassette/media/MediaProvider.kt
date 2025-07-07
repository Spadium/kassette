package com.spadium.kassette.media

abstract class MediaProvider {
    constructor() {
        this.init()
    }

    open var state: MediaManager.MediaState = MediaManager.MediaState.OTHER

    abstract fun getServiceName(): String
    @Deprecated(
        "Constructors will initialize providers since git commit 964028d3! This will be removed by release!",
        ReplaceWith("constructor()")
    )
    open fun init() {}

    abstract fun destroy()

    abstract fun getMedia(): MediaInfo

    abstract suspend fun update()
}