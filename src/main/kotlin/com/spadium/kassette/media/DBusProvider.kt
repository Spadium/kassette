package com.spadium.kassette.media

import net.minecraft.util.Util

class DBusProvider: MediaProvider {
    override val info: MediaInfo
        get() = TODO("Not yet implemented")
    override val availableCommands: List<String>
        get() = TODO("Not yet implemented")

    constructor() {
        if (Util.getOperatingSystem() != Util.OperatingSystem.LINUX) {
            throw UnsupportedOperationException("Cannot use DBus on Windows/macOS for media information!")
        }
    }

    override fun destroy() {
        TODO("Not yet implemented")
    }

    override suspend fun update() {
        TODO("Not yet implemented")
    }
}