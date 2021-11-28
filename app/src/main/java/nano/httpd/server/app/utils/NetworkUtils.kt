package nano.httpd.server.app.utils

import android.content.Context
import android.net.wifi.WifiManager


object NetworkUtils {

    fun isWiFiEnabled(context: Context): Boolean {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.isWifiEnabled
    }

    fun getLocalIpStr(context: Context): String {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        return intToIpAddr(wifiInfo.ipAddress)
    }

    fun intToIpAddr(ip: Int): String {
        return ((ip and 0xFF).toString() + "." + (ip shr 8 and 0xFF) + "." + (ip shr 16 and 0xFF) + "." + (ip shr 24 and 0xFF))
    }
}