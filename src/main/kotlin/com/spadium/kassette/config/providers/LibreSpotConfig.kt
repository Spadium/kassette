package com.spadium.kassette.config.providers

import com.spotify.connectstate.Connect
import kotlinx.serialization.Serializable
//Connect.DeviceType.COMPUTER, "Kassette LibreSpot Provider", "", "",
//            0
@Serializable
data class LibreSpotConfig(
    var deviceType: Connect.DeviceType = Connect.DeviceType.COMPUTER,
    var deviceName: String = "Kassette LibreSpot Provider",
    var token: String = "",
    var deviceId: String = "",
    var listenPort: Short = 0
)