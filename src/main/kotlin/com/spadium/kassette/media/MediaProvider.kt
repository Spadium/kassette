package com.spadium.kassette.media

abstract class MediaProvider {
    abstract val info: MediaInfo
    abstract val availableCommands: List<String>

    open fun getServiceName(): String { return info.provider }

    abstract fun destroy()

    open fun getMedia(): MediaInfo { return info }

    open fun sendCommand(cmd: String, payload: Any?): Int { return 0 }

    abstract suspend fun update()
}