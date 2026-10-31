package com.example.service

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.SystemClock
import com.example.model.SystemDiagnostics
import java.util.concurrent.TimeUnit

class DeviceDiagnosticsService(private val context: Context) {

    fun getDiagnostics(): SystemDiagnostics {
        val (batteryPct, isCharging) = getBatteryInfo()
        val (usedMb, totalMb) = getMemoryInfo()
        val (netType, isConnected) = getNetworkInfo()
        val uptime = formatUptime(SystemClock.elapsedRealtime())

        return SystemDiagnostics(
            batteryPct = batteryPct,
            isCharging = isCharging,
            ramUsedMb = usedMb,
            ramTotalMb = totalMb,
            networkType = netType,
            isNetworkConnected = isConnected,
            uptimeFormatted = uptime,
            coreStatus = if (batteryPct > 15) "OPTIMAL" else "LOW POWER ALERT"
        )
    }

    private fun getBatteryInfo(): Pair<Int, Boolean> {
        val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus: Intent? = context.registerReceiver(null, ifilter)

        val level: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: 92
        val scale: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: 100
        val batteryPct = if (level >= 0 && scale > 0) (level * 100 / scale) else 92

        val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL

        return Pair(batteryPct, isCharging)
    }

    private fun getMemoryInfo(): Pair<Long, Long> {
        val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        actManager.getMemoryInfo(memInfo)

        val totalMb = memInfo.totalMem / (1024 * 1024)
        val availMb = memInfo.availMem / (1024 * 1024)
        val usedMb = (totalMb - availMb).coerceAtLeast(0)

        return Pair(usedMb, totalMb)
    }

    private fun getNetworkInfo(): Pair<String, Boolean> {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNet = cm.activeNetwork ?: return Pair("Offline", false)
        val caps = cm.getNetworkCapabilities(activeNet) ?: return Pair("Offline", false)

        return when {
            caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> Pair("Quantum Wi-Fi", true)
            caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> Pair("5G Cellular", true)
            caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> Pair("Direct Uplink", true)
            else -> Pair("Connected", true)
        }
    }

    private fun formatUptime(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
        return String.format("%02dh %02dm %02ds", hours, minutes, seconds)
    }
}
