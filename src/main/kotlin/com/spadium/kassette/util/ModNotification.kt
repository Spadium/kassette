package com.spadium.kassette.util

import net.minecraft.text.Text

data class ModNotification(
    val notificationType: NotificationType,
    val source: Text,
    val sourceType: SourceType,
    val message: Text,
    val detailedMessage: Text = Text.empty()
) {
    constructor(notificationType: NotificationType, source: Text, sourceType: SourceType, throwable: Throwable) : this(
        notificationType, source, sourceType,
        Text.literal(throwable.message),
        Text.literal(throwable.stackTraceToString())
    ) {

    }

    enum class SourceType {
        PROVIDER, THEME, MOD
    }
    enum class NotificationType(color: Int) {
        NOTICE(0xFF222288.toInt()), WARNING(0xFFDD9900.toInt()), ERROR(0xFF882222.toInt())
    }
}
