package com.spadium.kassette.config.providers

import kotlinx.serialization.Serializable

@Serializable
data class SpotifyConfig(
    var clientId: String = "",
    var clientSecret: String = "",
    var accessToken: String = "",
    var refreshToken: String = "",
    var createdAt: Long = 0L,
    var nextRefresh: Long = 0L,
    var ignoreRateLimits: Boolean = false
)
