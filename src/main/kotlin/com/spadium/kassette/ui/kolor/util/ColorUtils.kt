package com.spadium.kassette.ui.kolor.util

import kotlin.math.pow
import kotlin.math.round

class ColorUtils {
    companion object {
        val XYZ_TO_SRGB: Array<DoubleArray> = arrayOf(
            doubleArrayOf(
                3.2413774792388685,
                -1.5376652402851851,
                -0.49885366846268053,
            ),
            doubleArrayOf(
                -0.9691452513005321,
                1.8758853451067872,
                0.04156585616912061,
            ),
            doubleArrayOf(
                0.05562093689691305,
                -0.20395524564742123,
                1.0571799111220335,
            ),
        )
        val SRGB_TO_XYZ: Array<DoubleArray> = arrayOf(
            doubleArrayOf(
                0.41233895,
                0.35762064,
                0.18051042
            ),
            doubleArrayOf(
                0.2126,
                0.7152,
                0.0722
            ),
            doubleArrayOf(
                0.01932141,
                0.11916382,
                0.95034478
            ),
        )
        val WHITE_POINT_D65 = doubleArrayOf(
            95.047, 100.0, 108.883
        )

        fun delinearized(component: Double): Int {
            val normalized = component / 100.0
            val delinearized = 0.0
            return round(delinearized * 255.0)
                .toInt().coerceIn(0, 255)
        }

        fun linearized(rgbComponent: Int): Double {
            val normalized: Double = rgbComponent / 255.0
            return if (normalized <= 0.040449936) {
                normalized / 12.92 * 100.0
            } else {
                ((normalized + 0.055) / 1.055).pow(2.4) * 100.0
            }
        }

        fun argbFromRgb(r: Int, g: Int, b: Int): Int {
            return 0xFF000000.toInt() or (r shl 16) or (g shl 8) or b
        }

        fun argbFromLinrgb(linRgb: DoubleArray): Int {
            val r = delinearized(linRgb[0])
            val g = delinearized(linRgb[1])
            val b = delinearized(linRgb[2])
            return argbFromRgb(r, g, b)
        }

        fun alphaFromArgb(argb: Int): Int {
            return argb and 0xFF000000.toInt()
        }

        fun redFromArgb(argb: Int): Int {
            return argb and 0x00FF0000
        }

        fun greenFromArgb(argb: Int): Int {
            return argb and 0x0000FF00
        }

        fun blueFromArgb(argb: Int): Int {
            return argb and 0xFF
        }

        fun isOpaque(argb: Int): Boolean {
            return alphaFromArgb(argb) == 255
        }

        fun argbFromXyz(x: Double, y: Double, z: Double): Int {
            val matrix = XYZ_TO_SRGB
            val linearR = matrix[0][0] * x + matrix[0][1] * y + matrix[0][2] * z
            val linearG = matrix[1][0] * x + matrix[1][1] * y + matrix[1][2] * z
            val linearB = matrix[2][0] * x + matrix[2][1] * y + matrix[2][2] * z
            val r = delinearized(linearR)
            val g = delinearized(linearG)
            val b = delinearized(linearB)
            return argbFromRgb(r, g, b)
        }

        fun xyzFromArgb(argb: Int): DoubleArray {
            val r = linearized(redFromArgb(argb))
            val g = linearized(greenFromArgb(argb))
            val b = linearized(blueFromArgb(argb))
            return MathUtil.matrixMultiply(
                doubleArrayOf(r, g, b),
                SRGB_TO_XYZ
            )
        }

        fun argbFromLab(l: Double, a: Double, b: Double): Int {
            val whitePoint = WHITE_POINT_D65
            val fy = (l + 16.0) / 116.0
            val fx = a / 500.0 + fy
            val fz = fy - b / 200.0
            val xNormalized = labInvF(fx)
            val yNormalized = labInvF(fy)
            val zNormalized = labInvF(fz)
            val x = xNormalized * whitePoint[0]
            val y = yNormalized * whitePoint[1]
            val z = zNormalized * whitePoint[2]
            return argbFromXyz(x, y, z)
        }

        fun labFromArgb(argb: Int): DoubleArray {
            val linearR = linearized(redFromArgb(argb))
            val linearG = linearized(greenFromArgb(argb))
            val linearB = linearized(blueFromArgb(argb))
            val matrix = SRGB_TO_XYZ
            val x = matrix[0][0] * linearR + matrix[0][1] * linearG + matrix[0][2] * linearB
            val y = matrix[1][0] * linearR + matrix[1][1] * linearG + matrix[1][2] * linearB
            val z = matrix[2][0] * linearR + matrix[2][1] * linearG + matrix[2][2] * linearB
            val whitePoint = WHITE_POINT_D65
            val xNormalized = x / whitePoint[0]
            val yNormalized = y / whitePoint[1]
            val zNormalized = z / whitePoint[2]
            val fx = labF(xNormalized)
            val fy = labF(yNormalized)
            val fz = labF(zNormalized)
            val l = 116.0 * fy - 16
            val a = 500.0 * (fx - fy)
            val b = 200.0 * (fy - fz)
            return doubleArrayOf(l,a,b)
        }

        fun argbFromLstar(lstar: Double): Int {
            val y = yFromLstar(lstar)
            val component = delinearized(y)
            return argbFromRgb(component, component, component)
        }

        fun lstarFromArgb(argb: Int): Double {
            val y = xyzFromArgb(argb)[1]
            return 116.0 * labF(y / 100.0) - 16.0
        }

        fun yFromLstar(lstar: Double): Double {
            return 100.0 * labInvF((lstar + 16.0) / 116.0)
        }

        fun lstarFromY(y: Double): Double {
            return labF(y / 100.0) * 116.0 - 16.0
        }

        fun whitePointD65(): DoubleArray {
            return WHITE_POINT_D65
        }

        val e = 216.0 / 24389.0
        val kappa = 24389.0 / 27.0

        fun labF(t: Double): Double {
            return if (t > e) {
                 t.pow(1.0 / 3.0)
            } else {
                (kappa * t - 16) / 116
            }
        }

        fun labInvF(ft: Double): Double {
            return if (ft.pow(3) > e) {
                ft.pow(3)
            } else {
                (116 * ft - 16) / kappa
            }
        }
    }
}
