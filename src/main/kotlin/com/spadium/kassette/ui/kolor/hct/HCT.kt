package com.spadium.kassette.ui.kolor.hct

class HCT {
    var hue: Double?
    var chroma: Double?
    var tone: Double?

    constructor(hue: Double, chroma: Double, tone: Double) {
        this.hue = hue
        this.chroma = chroma
        this.tone = tone
    }

    fun isBlue(hue: Double) {

    }

    fun fromInt(argb: Int): HCT {
        return HCT(0.0, 0.0, 0.0)
    }

    fun from(h: Double, c: Double, t: Double): HCT {
        return HCT(h, c, t)
    }
}