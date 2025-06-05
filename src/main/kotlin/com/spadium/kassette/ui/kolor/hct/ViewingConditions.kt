package com.spadium.kassette.ui.kolor.hct

import com.spadium.kassette.ui.kolor.util.MathUtil
import kotlin.math.*

class ViewingConditions {
    companion object {
        val DEFAULT: ViewingConditions = ViewingConditions(
            n = TODO(),
            aw = TODO(),
            nbb = TODO(),
            ncb = TODO(),
            c = TODO(),
            nc = TODO(),
            rgbD = TODO(),
            fl = TODO(),
            flRoot = TODO(),
            z = TODO()
        )

        fun make(
            whitePoint: DoubleArray, adaptingLuminance: Double,
            backgroundLstar: Double, surround: Double,
            discountingIlluminant: Boolean, t: Boolean
        ): ViewingConditions {
            var backgroundLstar = backgroundLstar
            backgroundLstar = max(0.1, backgroundLstar)

            var matrix: Array<DoubleArray> = arrayOf(doubleArrayOf(0.0))
            val xyz: DoubleArray = whitePoint
            TODO()
        }

//        fun make(
//            whitePoint: DoubleArray,
//            adaptingLuminance: Double,
//            backgroundLstar: Double,
//            surround: Double,
//            discountingIlluminant: Boolean
//        ): ViewingConditions {
            // A background of pure black is non-physical and leads to infinities that represent the idea
            // that any color viewed in pure black can't be seen.
//            var backgroundLstar = backgroundLstar
//            backgroundLstar = max(0.1, backgroundLstar)
            // Transform white point XYZ to 'cone'/'rgb' responses
//            val matrix = Cam16.XYZ_TO_CAM16RGB
//            val xyz = whitePoint
//            val rW = (xyz[0] * matrix[0]!![0]) + (xyz[1] * matrix[0]!![1]) + (xyz[2] * matrix[0]!![2])
//            val gW = (xyz[0] * matrix[1]!![0]) + (xyz[1] * matrix[1]!![1]) + (xyz[2] * matrix[1]!![2])
//            val bW = (xyz[0] * matrix[2]!![0]) + (xyz[1] * matrix[2]!![1]) + (xyz[2] * matrix[2]!![2])
//            val f = 0.8 + (surround / 10.0)
//            val c =
//                if (f >= 0.9)
//                    MathUtil.lerp(0.59, 0.69, ((f - 0.9) * 10.0))
//                else
//                    MathUtil.lerp(0.525, 0.59, ((f - 0.8) * 10.0))
//            var d =
//                if (discountingIlluminant)
//                    1.0
//                else
//                    f * (1.0 - ((1.0 / 3.6) * exp((-adaptingLuminance - 42.0) / 92.0)))
//            d = MathUtils.clampDouble(0.0, 1.0, d)
//            val nc = f
//            val rgbD =
//                doubleArrayOf(
//                    d * (100.0 / rW) + 1.0 - d, d * (100.0 / gW) + 1.0 - d, d * (100.0 / bW) + 1.0 - d
//                )
//            val k = 1.0 / (5.0 * adaptingLuminance + 1.0)
//            val k4 = k * k * k * k
//            val k4F = 1.0 - k4
//            val fl = (k4 * adaptingLuminance) + (0.1 * k4F * k4F * cbrt(5.0 * adaptingLuminance))
//            val n = (ColorUtils.yFromLstar(backgroundLstar) / whitePoint[1])
//            val z = 1.48 + sqrt(n)
//            val nbb = 0.725 / n.pow(0.2)
//            val ncb = nbb
//            val rgbAFactors =
//                doubleArrayOf(
//                    (fl * rgbD[0] * rW / 100.0).pow(0.42),
//                    (fl * rgbD[1] * gW / 100.0).pow(0.42),
//                    (fl * rgbD[2] * bW / 100.0).pow(0.42)
//                )

//            val rgbA =
//                doubleArrayOf(
//                    (400.0 * rgbAFactors[0]) / (rgbAFactors[0] + 27.13),
//                    (400.0 * rgbAFactors[1]) / (rgbAFactors[1] + 27.13),
//                    (400.0 * rgbAFactors[2]) / (rgbAFactors[2] + 27.13)
//                )

//            val aw = ((2.0 * rgbA[0]) + rgbA[1] + (0.05 * rgbA[2])) * nbb
//            return ViewingConditions(
//                n,
//                aw,
//                nbb,
//                ncb,
//                c,
//                nc,
//                rgbD,
//                fl,
//                fl.pow(0.25),
//                z
//            )
//        }
    }

    private var aw = 0.0
    private var nbb = 0.0
    private var ncb = 0.0
    private var c = 0.0
    private var nc = 0.0
    private var n = 0.0
    private var rgbD: DoubleArray? = null
    private var fl = 0.0
    private var flRoot = 0.0
    private var z = 0.0

    private constructor(
        n: Double,  aw: Double, nbb: Double,
        ncb: Double, c: Double, nc: Double,
        rgbD: DoubleArray, fl: Double, flRoot: Double,
        z: Double
    ) {
        this.n = n
        this.aw = aw
        this.nbb = nbb
        this.ncb = ncb
        this.c = c
        this.nc = nc
        this.rgbD = rgbD
        this.fl = fl
        this.flRoot = flRoot
        this.z = z
    }
}