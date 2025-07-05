package com.spadium.kassette

import kotlinx.serialization.Serializable

@Serializable
data class ModInfo(
    val buildType: BuildTypes,
    val gitCommitId: String,
    val gitBranchRef: String,
    val buildDate: Long
) {
    enum class BuildTypes {
        RELEASE, DEV, CI
    }
}