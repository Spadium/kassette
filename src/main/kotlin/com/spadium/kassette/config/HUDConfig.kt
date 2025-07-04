package com.spadium.kassette.config

import kotlinx.serialization.Serializable

@Serializable
data class HUDConfig(
    var width: Int,
    var height: Int,
    val imageSize: Int,
    var backgroundColor: IntArray,
    var borderColor: IntArray,
    var textSpeed: Int,
    var fancyTextSpeed: Int,
    var lineSpacing: Int,
    var progressBarThickness: Int,
    var showCover: Boolean,
    var fancyText: Boolean,
    var progressType: ProgressType,
) {
    @Serializable
    enum class ProgressType {
        BAR, TEXT, NONE,
        VERYLONGOPTIONTHATDEFAULTSTOBARMEANTFORDEBUGGINGPLEASEDONTUSEPLEASENO,
        OTHERVERYLONGOPTIONTHATDEFAULTSTOTEXTMEANTFORDEBUGGINGPLEASEDONTUSEOK,
        PLEASEDONTUSETHISVERYLONGOPTIONTHATHASNOPROGRESSTYPEMEANTFORDEBUGGING,
    }
}
