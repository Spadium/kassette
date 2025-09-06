package com.spadium.kassette.config.overlays

import kotlinx.serialization.Serializable

@Serializable
data class MasterOverlayConfig(
    val default: DefaultOverlayConfig
)
