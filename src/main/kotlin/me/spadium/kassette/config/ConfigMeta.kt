package me.spadium.kassette.config

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ConfigMeta(
    val configCategory: String,
    val type: ConfigType
) {
    enum class ConfigType(val path: String) {
        PROVIDER("providers/"), OVERLAY("overlays/"), MAIN("")
    }
}
