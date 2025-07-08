package com.spadium.kassette.util

import com.spadium.kassette.Kassette
import kotlinx.io.bytestring.getByteString
import net.minecraft.client.texture.NativeImage
import net.minecraft.util.PngMetadata
import org.intellij.lang.annotations.MagicConstant
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryUtil
import java.awt.image.BufferedImage
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.imageio.ImageIO

class ImageUtils {


    companion object {
        @JvmStatic
        fun validateImage(buffer: ByteBuffer, originalOrder: ByteOrder) {
            buffer.order(ByteOrder.BIG_ENDIAN)

            if (buffer.getLong(0) == -10977661529601466) {
                buffer.rewind()
            } else {
                buffer.rewind()
                buffer.order(originalOrder)
                PngMetadata.validate(buffer)
            }
        }

        fun loadImageIOImage(input: InputStream): NativeImage {
            val bufferedImage: BufferedImage = ImageIO.read(input)
            val nativeImage: NativeImage = NativeImage(bufferedImage.width, bufferedImage.height, true)
            for (x in 0..bufferedImage.width-1) {
                for (y in 0..bufferedImage.height-1) {
                    nativeImage.setColor(x, y, rgbToRgba(bufferedImage.getRGB(x, y)))
                }
            }
            return nativeImage
        }

        fun rgbToRgba(rgb: Int): Int {
            return (0xFF000000.or(rgb.toLong())).toInt()
        }

        fun loadGenericImage(bytes: ByteArray): NativeImage {
            return loadGenericImage(bytes, NativeImage.Format.RGBA)
        }

        fun loadGenericImage(bytes: ByteArray, format: NativeImage.Format): NativeImage {
            val imgBuf = MemoryUtil.memAlloc(bytes.size)
            imgBuf.put(bytes)
            imgBuf.flip()
            return loadStbImage(imgBuf, format)
        }

        fun loadStbImage(buf: ByteBuffer, format: NativeImage.Format): NativeImage {
            val widthBuf = MemoryUtil.memAllocInt(1)
            val heightBuf = MemoryUtil.memAllocInt(1)
            val channelBuf = MemoryUtil.memAllocInt(1)
            val imgBuf = STBImage.stbi_load_from_memory(
                buf, widthBuf, heightBuf, channelBuf, format.channelCount
            )
            if (imgBuf == null) {
                throw IOException("Failed to decode image / null buffer! Reason: ${STBImage.stbi_failure_reason()}")
            }
            return NativeImage(
                format,
                widthBuf.get(0), heightBuf.get(0),
                false, MemoryUtil.memAddress(imgBuf)
            )
        }
    }
}