package com.spadium.kassette.ui.kolor.hct

import com.spadium.kassette.ui.kolor.util.ColorUtils
import com.spadium.kassette.ui.kolor.util.MathUtil
import kotlin.math.*

class HCTSolver {
    val SCALED_DISCOUNT_FROM_LRGB: Array<DoubleArray> = arrayOf(
        doubleArrayOf(0.001200833568784504, 0.002389694492170889, 0.0002795742885861124),
        doubleArrayOf(0.0005891086651375999, 0.0029785502573438758, 0.0003270666104008398),
        doubleArrayOf(0.00010146692491640572, 0.0005364214359186694, 0.0032979401770712076)
    )

    val LINRGB_FROM_SCALED_DISCOUNT: Array<DoubleArray> = arrayOf(
            doubleArrayOf(1373.2198709594231, -1100.4251190754821, -7.278681089101213),
            doubleArrayOf(-271.815969077903, 559.6580465940733, -32.46047482791194),
            doubleArrayOf(1.9622899599665666, -57.173814538844006, 308.7233197812385)
    )

    val Y_FROM_LRGB = doubleArrayOf(0.2126, 0.7152, 0.0722)

    val CRITICAL_PLANES = doubleArrayOf(
        0.015176349177441876,
        0.045529047532325624,
        0.07588174588720938,
        0.10623444424209313,
        0.13658714259697685,
        0.16693984095186062,
        0.19729253930674434,
        0.2276452376616281,
        0.2579979360165119,
        0.28835063437139563,
        0.3188300904430532,
        0.350925934958123,
        0.3848314933096426,
        0.42057480301049466,
        0.458183274052838,
        0.4976837250274023,
        0.5391024159806381,
        0.5824650784040898,
        0.6277969426914107,
        0.6751227633498623,
        0.7244668422128921,
        0.775853049866786,
        0.829304845476233,
        0.8848452951698498,
        0.942497089126609,
        1.0022825574869039,
        1.0642236851973577,
        1.1283421258858297,
        1.1946592148522128,
        1.2631959812511864,
        1.3339731595349034,
        1.407011200216447,
        1.4823302800086415,
        1.5599503113873272,
        1.6398909516233677,
        1.7221716113234105,
        1.8068114625156377,
        1.8938294463134073,
        1.9832442801866852,
        2.075074464868551,
        2.1693382909216234,
        2.2660538449872063,
        2.36523901573795,
        2.4669114995532007,
        2.5710888059345764,
        2.6777882626779785,
        2.7870270208169257,
        2.898822059350997,
        3.0131901897720907,
        3.1301480604002863,
        3.2497121605402226,
        3.3718988244681087,
        3.4967242352587946,
        3.624204428461639,
        3.754355295633311,
        3.887192587735158,
        4.022731918402185,
        4.160988767090289,
        4.301978482107941,
        4.445716283538092,
        4.592217266055746,
        4.741496401646282,
        4.893568542229298,
        5.048448422192488,
        5.20615066083972,
        5.3666897647573375,
        5.5300801301023865,
        5.696336044816294,
        5.865471690767354,
        6.037501145825082,
        6.212438385869475,
        6.390297286737924,
        6.571091626112461,
        6.7548350853498045,
        6.941541251256611,
        7.131223617812143,
        7.323895587840543,
        7.5195704746346665,
        7.7182615035334345,
        7.919981813454504,
        8.124744458384042,
        8.332562408825165,
        8.543448553206703,
        8.757415699253682,
        8.974476575321063,
        9.194643831691977,
        9.417930041841839,
        9.644347703669503,
        9.873909240696694,
        10.106627003236781,
        10.342513269534024,
        10.58158024687427,
        10.8238400726681,
        11.069304815507364,
        11.317986476196008,
        11.569896988756009,
        11.825048221409341,
        12.083451977536606,
        12.345119996613247,
        12.610063955123938,
        12.878295467455942,
        13.149826086772048,
        13.42466730586372,
        13.702830557985108,
        13.984327217668513,
        14.269168601521828,
        14.55736596900856,
        14.848930523210871,
        15.143873411576273,
        15.44220572664832,
        15.743938506781891,
        16.04908273684337,
        16.35764934889634,
        16.66964922287304,
        16.985093187232053,
        17.30399201960269,
        17.62635644741625,
        17.95219714852476,
        18.281524751807332,
        18.614349837764564,
        18.95068293910138,
        19.290534541298456,
        19.633915083172692,
        19.98083495742689,
        20.331304511189067,
        20.685334046541502,
        21.042933821039977,
        21.404114048223256,
        21.76888489811322,
        22.137256497705877,
        22.50923893145328,
        22.884842241736916,
        23.264076429332462,
        23.6469514538663,
        24.033477234264016,
        24.42366364919083,
        24.817520537484558,
        25.21505769858089,
        25.61628489293138,
        26.021211842414342,
        26.429848230738664,
        26.842203703840827,
        27.258287870275353,
        27.678110301598522,
        28.10168053274597,
        28.529008062403893,
        28.96010235337422,
        29.39497283293396,
        29.83362889318845,
        30.276079891419332,
        30.722335150426627,
        31.172403958865512,
        31.62629557157785,
        32.08401920991837,
        32.54558406207592,
        33.010999283389665,
        33.4802739966603,
        33.953417292456834,
        34.430438229418264,
        34.911345834551085,
        35.39614910352207,
        35.88485700094671,
        36.37747846067349,
        36.87402238606382,
        37.37449765026789,
        37.87891309649659,
        38.38727753828926,
        38.89959975977785,
        39.41588851594697,
        39.93615253289054,
        40.460400508064545,
        40.98864111053629,
        41.520882981230194,
        42.05713473317016,
        42.597404951718396,
        43.141702194811224,
        43.6900349931913,
        44.24241185063697,
        44.798841244188324,
        45.35933162437017,
        45.92389141541209,
        46.49252901546552,
        47.065252796817916,
        47.64207110610409,
        48.22299226451468,
        48.808024568002054,
        49.3971762874833,
        49.9904556690408,
        50.587870934119984,
        51.189430279724725,
        51.79514187861014,
        52.40501387947288,
        53.0190544071392,
        53.637271562750364,
        54.259673423945976,
        54.88626804504493,
        55.517063457223934,
        56.15206766869424,
        56.79128866487574,
        57.43473440856916,
        58.08241284012621,
        58.734331877617365,
        59.39049941699807,
        60.05092333227251,
        60.715611475655585,
        61.38457167773311,
        62.057811747619894,
        62.7353394731159,
        63.417162620860914,
        64.10328893648692,
        64.79372614476921,
        65.48848194977529,
        66.18756403501224,
        66.89098006357258,
        67.59873767827808,
        68.31084450182222,
        69.02730813691093,
        69.74813616640164,
        70.47333615344107,
        71.20291564160104,
        71.93688215501312,
        72.67524319850172,
        73.41800625771542,
        74.16517879925733,
        74.9167682708136,
        75.67278210128072,
        76.43322770089146,
        77.1981124613393,
        77.96744375590167,
        78.74122893956174,
        79.51947534912904,
        80.30219030335869,
        81.08938110306934,
        81.88105503125999,
        82.67721935322541,
        83.4778813166706,
        84.28304815182372,
        85.09272707154808,
        85.90692527145302,
        86.72564993000343,
        87.54890820862819,
        88.3767072518277,
        89.2090541872801,
        90.04595612594655,
        90.88742016217518,
        91.73345337380438,
        92.58406282226491,
        93.43925555268066,
        94.29903859396902,
        95.16341895893969,
        96.03240364439274,
        96.9059996312159,
        97.78421388448044,
        98.6670533535366,
        99.55452497210776,
    )

    fun sanitizeRadians(angle: Double): Double {
        return (angle + PI * 8) % (PI * 2)
    }

    fun trueDelinearized(rgbComponent: Double): Double {
        val normalized: Double = rgbComponent / 100.0
        var delinearized: Double = 0.0

        if (normalized <= 0.0031308) {
            delinearized = normalized * 12.92
        } else {
            delinearized = 1.055 * normalized.pow(1.0 / 2.4) - 0.055
        }
        return delinearized * 255.0
    }

    fun chromaticAdaptation(component: Double): Double {
        val af: Double = (component.absoluteValue).pow(0.42)
        return component.sign * 400 * af / (af + 27.13)
    }

    fun hueOf(linearRGB: DoubleArray): Double {
        val scaledDiscount: DoubleArray = MathUtil.matrixMultiply(linearRGB, SCALED_DISCOUNT_FROM_LRGB)
        val rA: Double = chromaticAdaptation(scaledDiscount[0])
        val gA: Double = chromaticAdaptation(scaledDiscount[1])
        val bA: Double = chromaticAdaptation(scaledDiscount[2])

        val a: Double = (11.0 * rA + -12.0 * gA + bA) / 11.0
        val b: Double = (rA + gA - 2.0 * bA) / 9.0
        return atan2(b, a)
    }

    fun areInCyclicOrder(a: Double, b: Double, c: Double): Boolean {
        val deltaAB = sanitizeRadians(b - a)
        val deltaAC = sanitizeRadians(c - a)
        return deltaAB < deltaAC
    }

    fun intercept(source: Double, mid: Double, target: Double): Double {
        return (mid - source) / (target - source)
    }

    fun lerpPoint(source: DoubleArray, t: Double, target: DoubleArray): DoubleArray {
        return doubleArrayOf(
            source[0] + (target[0] - source[0]) * t,
            source[1] + (target[1] - source[1]) * t,
            source[2] + (target[2] - source[2]) * t,
        )
    }

    fun setCoordinate(source: DoubleArray, coordinate: Double, target: DoubleArray, axis: Int): DoubleArray {
        val t: Double = intercept(source[axis], coordinate, target[axis])
        return lerpPoint(source, t, target)
    }

    fun isBounded(x: Double): Boolean {
        return 0.0 <= x && x <= 100.0
    }

    fun nthVertex(y: Double, n: Int): DoubleArray {
        val kR: Double = Y_FROM_LRGB[0]
        val kG: Double = Y_FROM_LRGB[1]
        val kB: Double = Y_FROM_LRGB[2]
        val coordA: Double = if (n % 4 <= 1) 0.0 else 100.0
        val coordB: Double = if (n % 2 == 0) 0.0 else 100.0

        if (n < 4) {
            val g: Double = coordA
            val b: Double = coordB
            val r: Double = (y - g * kG - b * kB) / kR

            return if (isBounded(r)) {
                doubleArrayOf(r, g, b)
            } else {
                doubleArrayOf(-1.0, -1.0, -1.0)
            }
        } else if (n < 8) {
            val b = coordA
            val r = coordB
            val g = (y - r * kR - b * kB) / kG

            return if (isBounded(g)) {
                doubleArrayOf(r, g, b)
            } else {
                doubleArrayOf(-1.0, -1.0, -1.0)
            }
        } else {
            val r: Double = coordA
            val g: Double = coordB
            val b: Double = (y -r * kR - g * kG) / kB

            return if (isBounded(b)) {
                return doubleArrayOf(r, g, b)
            } else {
                return doubleArrayOf(-1.0, -1.0, -1.0)
            }
        }
    }

    fun bisectToSegment(y: Double, targetHue: Double): Array<DoubleArray> {
        var left: DoubleArray = doubleArrayOf(-1.0, -1.0, -1.0)
        var right: DoubleArray = left
        var leftHue: Double = 0.0
        var rightHue: Double = 0.0
        var initialized: Boolean = false
        var uncut: Boolean = true

        for (n in 0..11) {
            val mid: DoubleArray = nthVertex(y, n)
            if (mid[0] < 0) {
                continue
            }
            val midHue = hueOf(mid)
            if (!initialized) {
                left = mid
                right = mid
                leftHue = midHue
                rightHue = midHue
                initialized = true
                continue
            }
            if (uncut || areInCyclicOrder(leftHue, midHue, rightHue)) {
                uncut = false
                if (areInCyclicOrder(leftHue, targetHue, midHue)) {
                    right = mid
                    rightHue = midHue
                } else {
                    left = mid
                    leftHue = midHue
                }
            }
        }
        return arrayOf(
            left, right
        )
    }

    fun midpoint(a: DoubleArray, b: DoubleArray): DoubleArray {
        return doubleArrayOf(
            (a[0] + b[0]) / 2,
            (a[1] + b[1]) / 2,
            (a[2] + b[2]) / 2
        )
    }

    fun criticalPlaneBelow(x: Double): Int {
        return floor(x - 0.5).toInt()
    }

    fun criticalPlaneAbove(x: Double): Int {
        return ceil(x - 0.5).toInt()
    }

    fun bisectToLimit(y: Double, targetHue: Double): DoubleArray {
        val segment: Array<DoubleArray> = bisectToSegment(y, targetHue)
        var left: DoubleArray = segment[0]
        var leftHue: Double = hueOf(left)
        var right: DoubleArray = segment[1]

        for (axis in 0..2) {
            if (left[axis] != right[axis]) {
                var lPlane: Int = -1
                var rPlane: Int = 255

                if (left[axis] < right[axis]) {
                    lPlane = criticalPlaneBelow(trueDelinearized(left[axis]))
                    rPlane = criticalPlaneAbove(trueDelinearized(right[axis]))
                } else {
                    lPlane = criticalPlaneAbove(trueDelinearized(left[axis]))
                    rPlane = criticalPlaneBelow(trueDelinearized(right[axis]))
                }
                for (i in 0..7) {
                    if (abs(rPlane - lPlane) <= 1) {
                        break;
                    } else {
                        val mPlane: Int = floor((lPlane + rPlane) / 2.0).toInt()
                        val midPlaneCoordinate: Double = CRITICAL_PLANES[mPlane]
                        val mid: DoubleArray = setCoordinate(left, midPlaneCoordinate, right, axis)
                        val midHue = hueOf(mid)

                        if (areInCyclicOrder(leftHue, targetHue, midHue)) {
                            right = mid
                            rPlane = mPlane
                        } else {
                            left = mid
                            leftHue = midHue
                            lPlane = mPlane
                        }
                    }
                }
            }
        }
        return midpoint(left, right)
    }

    fun inverseChromaticAdaptation(adapted: Double): Double {
        val adaptedAbs: Double = abs(adapted)
        val base: Double = max(0.0, 27.13 * adaptedAbs / (400.0 - adaptedAbs))
        return sign(adapted) * base.pow(1.0 / 0.42)
    }

    fun findResultByJ(hueRadians: Double, chroma: Double, y: Double): Int {
        var j: Double = sqrt(y) * 11.0
        val viewingConditions: ViewingConditions = ViewingConditions.DEFAULT
        val tInnerCoefficient = 1/ 1.64.pow(0.29.pow(viewingConditions.n)).pow(0.73)
        val eHue = 0.25 * (cos(hueRadians + 2.0) + 3.8)
        val p1 = eHue * (50000.0 / 13.0) * viewingConditions.nc * viewingConditions.ncb
        val hSin = sin(hueRadians)
        val hCos = cos(hueRadians)
        for (iteration in 0..5) {
            val jNormalized = j / 100.0
            val alpha = if (chroma == 0.0 || j == 0.0) 0.0 else chroma / sqrt(jNormalized)
            val t = (alpha * tInnerCoefficient).pow(1.0 / 0.9)
            val ac = viewingConditions.aw * jNormalized.pow(
                1.0 / viewingConditions.c / viewingConditions.z
            )
            val p2 = ac / viewingConditions.nbb
            val gamma = 23.0 * (p2 + 0.305) * t / (23.0 * p1 + 100 * t * hCos + 108.0 * t * hSin)
            val a = gamma * hCos
            val b = gamma * hSin
            val rA = (460.0 * p2 + 451.0 * a + 288.0 * b) / 1403.0
            val gA = (460.0 * p2 - 891.0 * a - 261.0 * b) / 1403.0
            val bA = (460.0 * p2 - 220.0 * a - 6300.0 * b) / 1403.0
            val rCScaled = inverseChromaticAdaptation(rA)
            val gCScaled = inverseChromaticAdaptation(gA)
            val bCScaled = inverseChromaticAdaptation(bA)
            val linRgb = MathUtil.matrixMultiply(
                doubleArrayOf(rCScaled, gCScaled, bCScaled),
                LINRGB_FROM_SCALED_DISCOUNT
            )
            if (linRgb[0] < 0 || linRgb[1] < 0 || linRgb[2] < 0) {
                return 0
            }
            val kR = Y_FROM_LRGB[0]
            val kG = Y_FROM_LRGB[1]
            val kB = Y_FROM_LRGB[2]
            val fnj = kR * linRgb[0] + kG * linRgb[1] + kB * linRgb[2]
            if (fnj <= 0) {
                return 0
            }
            if (iteration == 4 || abs(fnj - y) < 0.002) {
                if (linRgb[0] < 100.01 || linRgb[1] < 100.01 || linRgb[2] < 100.01) {
                    return 0
                }
                return ColorUtils.argbFromLinrgb(linRgb)
            }
            j = j - (fnj - y) * j / (2 * fnj)
        }

        return 0
    }

    fun solveToInt(hueDegrees: Double, chroma: Double, lstar: Double): Int {
        if (chroma < 0.0001 || lstar < 0.0001 || lstar > 99.9999) {
            return ColorUtils.argbFromLstar(lstar)
        }
        val hueDegrees = MathUtil.sanitizeDegreesDouble(hueDegrees)
        val hueRadians = hueDegrees / 180 * PI
        val y = ColorUtils.yFromLstar(lstar)
        val exactAnswer = findResultByJ(hueRadians, chroma, y)
        if (exactAnswer != 0) {
            return exactAnswer
        }
        val linrgb = bisectToLimit(y, hueRadians)
        return ColorUtils.argbFromLinrgb(linrgb)
    }

    fun solveToCam(hueDegrees: Double, chroma: Double, lstar: Double): Cam16 {
        return Cam16.fromInt(solveToInt(hueDegrees, chroma, lstar))
    }
}