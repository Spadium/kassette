package com.spadium.kassette.ui.kolor.hct

import com.spadium.kassette.ui.kolor.util.ColorUtils
import kotlin.math.pow
import kotlin.math.sqrt

class Cam16 {
    val hue: Double
    val chroma: Double
    val j: Double
    val q: Double
    val m: Double
    val s: Double
    val jstar: Double
    val astar: Double
    val bstar: Double

    private constructor(
        hue: Double,
        chroma: Double,
        j: Double,
        q: Double,
        m: Double,
        s: Double,
        jstar: Double,
        astar: Double,
        bstar: Double
    ) {
        this.hue = hue
        this.chroma = chroma
        this.j = j
        this.q = q
        this.m = m
        this.s = s
        this.jstar = jstar
        this.astar = astar
        this.bstar = bstar
    }

    fun distance(other: Cam16): Double {
        val dJ = jstar - other.jstar
        val dA = astar - other.astar
        val dB = bstar - other.bstar
        val dEPrime = sqrt(dJ * dJ + dA * dA + dB * dB)
        return 1.41 * dEPrime.pow(0.63)
    }

    companion object {
        val XYZ_TO_CAM16RGB: Array<DoubleArray> = arrayOf(
            doubleArrayOf(0.401288, 0.650173, -0.051461),
            doubleArrayOf(-0.250268, 1.204414, 0.045854),
            doubleArrayOf(-0.002079, 0.048952, 0.953127)
        )

        val CAM16RGB_TO_XYZ: Array<DoubleArray> = arrayOf(
            doubleArrayOf(1.8620678, -1.0112547, 0.14918678),
            doubleArrayOf(0.38752654, 0.62144744, -0.00897398),
            doubleArrayOf(-0.01584150, -0.03412294, 1.0499644)
        )

        fun fromInt(argb: Int): Cam16 {
            return fromIntIntViewingConditions(argb, ViewingConditions.DEFAULT)
        }

        fun fromIntIntViewingConditions(argb: Int, conditions: ViewingConditions): Cam16 {
            val red = (argb and 0x00FF0000) ushr 16
            val green = (argb and 0x0000FF00) ushr 8
            val blue = (argb and 0x000000FF)
            val redL = ColorUtils.linearized(red)
            val greenL = ColorUtils.linearized(green)
            val blueL = ColorUtils.linearized(blue)
            val x = 0.41233895 * redL + 0.35762064 * greenL + 0.18051042 * blueL
            val y = 0.2126 * redL + 0.7152 * greenL + 0.0722 * blueL
            val z = 0.01932141 * redL + 0.11916382 * greenL + 0.95034478 * blueL

            return fromXyzInViewingConditions(x,y,z, conditions)
        }

        private fun fromXyzInViewingConditions(
            x: Double,
            y: Double,
            z: Double,
            conditions: ViewingConditions
        ): Cam16 {
            val matrix = XYZ_TO_CAM16RGB
            val rT = (x * matrix[0][0]) + (y * matrix[0][1]) + (z * matrix[0][2])
            val gT = (x * matrix[1][0]) + (y * matrix[1][1]) + (z * matrix[1][2])
            val bT = (x * matrix[2][0]) + (y * matrix[2][1]) + (z * matrix[2][2])

            val rD = conditions.rgbD[0] * rT
            val gD = conditions.rgbD[1] * gT
            val bD = conditions.rgbD[2] * bT


            TODO()
        }


    }
}