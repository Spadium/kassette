package com.spadium.kassette.ui.kolor.util

class MathUtil {
    companion object {
        fun lerp(start: Double, stop: Double, amount: Double): Double {
            return (1.0 - amount) * start + amount * stop
        }

        fun matrixMultiply(row: DoubleArray, matrix: Array<DoubleArray>): DoubleArray {
            val a: Double = row[0] * matrix[0][0] + row[1] * matrix[0][1] + row[2] * matrix[0][2]
            val b: Double = row[0] * matrix[1][0] + row[1] * matrix[1][1] + row[2] * matrix[1][2]
            val c: Double = row[0] * matrix[2][0] + row[1] * matrix[2][1] + row[2] * matrix[2][2]
            return doubleArrayOf(a, b, c)
        }

        fun sanitizeDegreesDouble(degrees: Double): Double {
            var deg = degrees % 360.0
            if (deg < 0) {
                deg = deg + 360.0
            }
            return deg
        }

        fun sanitizeDegreesInt(degrees: Int): Int {
            var deg = degrees % 360
            if (deg < 0) {
                deg = deg + 360
            }
            return deg
        }
    }
}