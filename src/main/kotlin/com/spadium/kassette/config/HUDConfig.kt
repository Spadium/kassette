package com.spadium.kassette.config

import kotlinx.serialization.Serializable

@Serializable
data class HUDConfig(
    var width: Int,
    var height: Int,
    var bgColor: Int,
    var fgColor: Int,
    var textSpeed: Int,
    var fancyTextSpeed: Int,
    var showCover: Boolean,
    var fancyText: Boolean,
    var progressType: ProgressType
) {
    @Serializable
    enum class ProgressType {
        BAR, TEXT
    }
}
