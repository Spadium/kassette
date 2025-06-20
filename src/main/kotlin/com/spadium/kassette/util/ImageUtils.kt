package com.spadium.kassette.util

import net.minecraft.client.texture.NativeImage
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryUtil
import java.io.IOException
import java.nio.ByteBuffer

class ImageUtils {


    companion object {
        fun loadGenericImage(bytes: ByteArray): NativeImage {
            val imgBuf = MemoryUtil.memAlloc(bytes.size)
            imgBuf.put(bytes)
            imgBuf.flip()
            print(imgBuf.getLong(0))
            return loadStbImage(imgBuf)
        }

        fun loadStbImage(buf: ByteBuffer): NativeImage {
            val widthBuf = MemoryUtil.memAllocInt(1)
            val heightBuf = MemoryUtil.memAllocInt(1)
            val channelBuf = MemoryUtil.memAllocInt(1)
            val imgBuf = STBImage.stbi_load_from_memory(
                buf, widthBuf, heightBuf, channelBuf, 0
            )
            if (imgBuf == null) {
                throw IOException("Failed to decode image / null buffer! Reason: ${STBImage.stbi_failure_reason()}")
            }
            return NativeImage(
                NativeImage.Format.RGB,
                widthBuf.get(0), heightBuf.get(0),
                false, MemoryUtil.memAddress(imgBuf)
            )
        }
    }
}