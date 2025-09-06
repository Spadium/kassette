package com.spadium.kassette.config.overlays

import kotlinx.serialization.Serializable

@Serializable
data class DefaultOverlayConfig(
    var width: Int,
    var height: Int,
    val imageSize: Int,
    var backgroundColor: IntArray,
    var borderColor: IntArray,
    val progressBackgroundColor: IntArray,
    val progressForegroundColor: IntArray,
    var textSpeed: Int,
    var fancyTextSpeed: Int,
    var lineSpacing: Int,
    var progressBarThickness: Int,
    var showCover: Boolean,
    var fancyText: Boolean,
    var progressType: ProgressType
) {
    @Serializable
    enum class ProgressType {
        BAR, TEXT, NONE,
        VERYLONGOPTIONTHATDEFAULTSTOBARMEANTFORDEBUGGINGPLEASEDONTUSEPLEASENO,
        OTHERVERYLONGOPTIONTHATDEFAULTSTOTEXTMEANTFORDEBUGGINGPLEASEDONTUSEOK,
        PLEASEDONTUSETHISVERYLONGOPTIONTHATHASNOPROGRESSTYPEMEANTFORDEBUGGING,
    }
}