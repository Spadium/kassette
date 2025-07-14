package com.spadium.kassette.util

import net.minecraft.client.texture.NativeImage
import net.minecraft.util.PngMetadata
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
        fun loadImageIOImage(input: InputStream, close: Boolean = false): NativeImage {
            val bufferedImage: BufferedImage = ImageIO.read(input)
            val nativeImage: NativeImage = NativeImage(bufferedImage.width, bufferedImage.height, true)
            nativeImage.format
            for (x in 0..bufferedImage.width-1) {
                for (y in 0..bufferedImage.height-1) {
                    // AA BB GG RR
                    nativeImage.setColor(x, y, rgbaToAbgr(bufferedImage.getRGB(x, y)))
                }
            }
            if (close)
                input.close()
            return nativeImage
        }

        @OptIn(ExperimentalStdlibApi::class)
        private fun rgbaToAbgr(rgb: Int): Int {
            val r = (rgb shr 16) and 0xFF
            val g = (rgb shr 8) and 0xFF
            val b = rgb and 0xFF
            val a = (rgb shr 24) and 0xFF

            return (a shl 24) or (b shl 16) or (g shl 8) or r
        }
    }
}