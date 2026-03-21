package me.spadium.kassette.media.mpris.interfaces

import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.annotations.DBusInterfaceName
import org.freedesktop.dbus.messages.DBusSignal

@DBusInterfaceName("org.mpris.MediaPlayer2.Player")
interface DBusPlayer {
    class Seeked(busPath: DBusPath, position: Int): DBusSignal(busPath, position)
}