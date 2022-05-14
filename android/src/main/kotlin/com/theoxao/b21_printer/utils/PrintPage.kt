package com.theoxao.b21_printer.utils

import java.util.*

class PrintPage {
    var width: Double = 0.0
    var height: Double = 0.0
    var labelType:Int = 1
    var density: Int = 3
    var quality: Int? = null
    var draw: List<Draw> = arrayListOf()
    class Draw {
        var type: String = "text"
        var content: String = ""
        var x: Double = 0.0
        var y: Double =0.0
        var x1: Double = 0.0
        var y1: Double =0.0
        var width: Double = 0.0
        var height: Double = 0.0
        var fontSize: Double = 0.0
        var letterSpace: Double = 0.0
        var lineSpace: Float = 0.0f
        var rotate: Int = 0
    }
}

enum class DrawType{
    TEXT,QRCODE,LINE;
}
fun drawType(type:String):DrawType?{
    return DrawType.values().firstOrNull { it.name == type.uppercase(Locale.getDefault()) }
}