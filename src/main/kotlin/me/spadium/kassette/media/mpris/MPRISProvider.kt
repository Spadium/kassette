package me.spadium.kassette.media.mpris

import me.spadium.kassette.media.MediaInfo
import me.spadium.kassette.media.MediaProvider
import net.minecraft.util.Util
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.connections.impl.DBusConnectionBuilder

class MPRISProvider: MediaProvider {
    override val info: MediaInfo
        get() = TODO("Not yet implemented")
    override val availableCommands: List<String>
        get() = TODO("Not yet implemented")
    var bus: DBusConnection? = null;

    constructor() {
        if (Util.getPlatform() != Util.OS.LINUX) {
            throw UnsupportedOperationException("Cannot use DBus on Windows/macOS for media information!")
        }
        bus = DBusConnectionBuilder.forSessionBus().build();
    }

    override fun destroy() {
        TODO("Not yet implemented")
    }

    override suspend fun update() {
        TODO("Not yet implemented")
    }
}