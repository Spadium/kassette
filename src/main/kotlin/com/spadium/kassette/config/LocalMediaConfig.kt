package com.spadium.kassette.config

import kotlinx.serialization.Serializable
import java.nio.file.Path

@Serializable
data class LocalMediaConfig(
    var basePath: Path,
    var ignoreDirectories: MutableList<Path>,
    var ignoreExtensions: MutableList<String>
)