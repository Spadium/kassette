package com.spadium.kassette.config.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object CustomIntSerializer  : KSerializer<Int> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "kotlin.Int",
        PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): Int {
        return 0
    }

    override fun serialize(encoder: Encoder, value: Int) {
        encoder.encodeString(value.toString())
    }
}