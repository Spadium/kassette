package com.spadium.kassette.util

import net.minecraft.network.chat.Component

data class ModNotification(
    val notificationType: NotificationType,
    val source: Component,
    val sourceType: SourceType,
    val message: Component,
    val detailedMessage: Component = Component.empty()
) {
    constructor(notificationType: NotificationType, source: Component, sourceType: SourceType, throwable: Throwable) : this(
        notificationType, source, sourceType,
        Component.literal(throwable.message),
        Component.literal(throwable.stackTraceToString())
    ) {

    }

    enum class SourceType {
        PROVIDER, THEME, MOD
    }
    enum class NotificationType(color: Int) {
        NOTICE(0xFF222288.toInt()), WARNING(0xFFDD9900.toInt()), ERROR(0xFF882222.toInt())
    }
}
