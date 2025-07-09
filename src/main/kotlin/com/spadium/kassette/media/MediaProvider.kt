package com.spadium.kassette.media

abstract class MediaProvider {
    abstract val info: MediaInfo

    abstract fun getServiceName(): String
    @Deprecated(
        "Constructors will initialize providers since git commit 964028d3! This will be removed by release!",
        ReplaceWith("constructor()")
    )

    abstract fun destroy()

    open fun getMedia(): MediaInfo { return info }

    open fun sendCommand(cmd: String, payload: Any): Int { return 0 }

    abstract suspend fun update()
}