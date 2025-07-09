package com.spadium.kassette.media

class PlaceholderProvider: MediaProvider {
    private var state: MediaManager.MediaState = MediaManager.MediaState.OTHER
    val art = MediaManager.getDefaultCoverArt()
    override val info: MediaInfo = _getMedia()
    override val availableCommands: List<String> = listOf()

    override fun getServiceName(): String {
        return "placeholder"
    }

    constructor() {
    }

    override fun destroy() {

    }

    private fun _getMedia(): MediaInfo {
        return MediaInfo(
            255,
            128,
            "View Kassette settings for more information", "N/A", "Kassette",
            art,
            "Placeholder",
            state
        )
    }

    override suspend fun update() {
    }
}