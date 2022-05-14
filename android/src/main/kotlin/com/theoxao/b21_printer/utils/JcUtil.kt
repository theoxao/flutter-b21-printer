package com.theoxao.b21_printer.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.gengcon.www.jcapi.api.JCAPI
import com.gengcon.www.jcapi.api.JCAPI.CallBack
import com.google.gson.Gson
import com.theoxao.b21_printer.utils.DrawType.*

class JcUtil(private val context: Context) {

    val TAG = "JcUtil"

    companion object {
        private const val KeyLastPrinterMac = "LastPrinterMac"
        private const val KeyLastPrinterName = "LastPrinterName"
        private const val KeyLastPrinterType = "LastPrinterType"

        @Volatile
        private var instance: JcUtil? = null

        //运行时加载对象
        fun getInstance(context: Context): JcUtil {
            if (instance == null) {
                synchronized(JcUtil::class.java) {
                    if (instance == null) {
                        instance = JcUtil(context)
                    }
                }
            }
            return instance!!
        }
    }

    private var api: JCAPI
    private val callback: JCAPI.CallBack = object : CallBack {
        override fun onConnectSuccess() {
            Toast.makeText(context, "连接打印机成功", Toast.LENGTH_SHORT).show();
        }

        override fun onConnectFail() {
            Toast.makeText(context, "连接打印机失败", Toast.LENGTH_SHORT).show();
        }

        override fun disConnect() {

        }

        override fun onAbnormalResponse(p0: Int) {
        }

        override fun electricityChange(p0: Int) {
        }
    }

    init {
        api = JCAPI.getInstance(context, callback)
    }

    private fun labelType(): String {
        return when (api.labelType) {
            1 -> "间隙纸"
            2 -> "黑标纸"
            3 -> "连续纸"
            else -> "未知或超时"
        }
    }

    private fun setLabelType(type: String): Any {
        api.labelType = type.toInt()
        return "success"
    }

    /**
     * type 1-15 分钟，2-30 分钟，3-45 分钟，4-60
     */
    private fun shutdownTime(): String {
        return when (api.autoShutDownTime) {
            1 -> "15分钟"
            2 -> "30分钟"
            3 -> "45分钟"
            5 -> "60分钟"
            else -> "未知"
        }
    }

    private fun setShutdownTime(type: String): String {
        api.setPrinterAutoShutdownTime(type.toInt())
        return "success"
    }

    private fun Any.str(): String {
        return this::class.java.declaredFields.joinToString("\n") {
            it.isAccessible = true
            it.name + ":" + it.get(this).toString()
        }
    }

    private fun getParams(type: Int): String {
        val method = JCAPI::class.java.superclass.getDeclaredMethod(
            "getPrinterAdvancedParameters",
            Int::class.java
        ).apply {
            isAccessible = true
        }
        return method.invoke(api, type).str()
    }

    private fun open(mac: String?) {
        if (mac == null) {
            Toast.makeText(context, "连接打印机失败", Toast.LENGTH_SHORT).show()
            return
        }
        api.openPrinterByAddressAsync(mac)
    }


    fun handle(method: String, args: Any?): Any {
        val arguments = (args as? Map<String, Any>) ?: mapOf()
        return when (method) {
            "open" -> open(arguments["mac"]?.toString())
            "print" -> printPage(arguments["pageInfo"]?.toString()!!)
            "info" -> {
                """{
                  "deviceSn":"${api.deviceSn}",
                   "hardwareVersion":"${api.hardWareVersion}",
                   "labelType":"${labelType()}",
                   "printDensity":"${api.printDensity}",
                   "shutdownTime":"${api.autoShutDownTime}",
                   "electricity":"${api.electricity}",
                   "softwareVersion":"${api.softWareVersion}",
                   "extraParam0": "${getParams(0)}",
                   "extraParam2": "${getParams(1)}"
                   }
                """.trimIndent()
            }
            else -> ""
        }
    }

    private val gson = Gson()

    private fun printPage(raw: String): Any {
        val page = gson.fromJson(raw, PrintPage::class.java)
        api.startJob(page.width, page.height, 0, page.labelType, page.density)
        api.startPage()
        page.draw.forEach {
            when (drawType(it.type)) {
                TEXT -> api.drawText(it.content,it.x , it.y, it.width, it.height, it.fontSize, it.letterSpace, it.lineSpace, 0, 0, it.rotate, true, "")
                QRCODE -> api.drawQrCode(it.content, it.x, it.y, it.width, it.rotate)
                LINE -> api.drawLine(it.x, it.y, it.x1, it.y1,it.width, it.rotate,false)
                else -> Log.e(TAG, "draw type not support:${it.type}")
            }
        }
        api.endPage()
        api.commitJob(1)
        api.endJob()
        return "success"
    }

}