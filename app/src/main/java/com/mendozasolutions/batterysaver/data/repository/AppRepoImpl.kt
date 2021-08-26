package com.mendozasolutions.batterysaver.data.repository

import android.app.ActivityManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import android.os.Process
import android.util.Log
import androidx.annotation.RequiresApi
import com.mendozasolutions.batterysaver.domain.repository.home.AppRepo
import java.io.DataOutputStream

class AppRepoImpl : AppRepo {
    val TAG = "HomeRepo"

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun getAppsProgressInfo(context: Context)  : List<UsageStats> {
        val statsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val time = System.currentTimeMillis()
        Log.i("HomeFragment", "Obteniendo la lista de apps")
        return statsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            time - (10000 * 1000),
            time
        )
    }

    override fun killAppProcess(context : Context,packageName: String) {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        Log.i(TAG, "Background Process $packageName")
        manager.killBackgroundProcesses(packageName)
        Log.i(TAG, "Closing background Process $packageName")


    }


}