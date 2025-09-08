package com.spadium.kassette.config.providers

import com.spotify.connectstate.Connect
import kotlinx.serialization.Serializable

@Serializable
data class LibreSpotConfig(
    var deviceType: Connect.DeviceType,
    var deviceName: String,
    var token: String,
    var deviceId: String,
    var listenPort: Short
)