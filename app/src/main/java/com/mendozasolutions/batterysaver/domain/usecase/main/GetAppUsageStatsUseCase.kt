package com.mendozasolutions.batterysaver.domain.usecase.main

import android.app.usage.UsageStats
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.mendozasolutions.batterysaver.data.model.App
import com.mendozasolutions.batterysaver.domain.repository.main.AppRepo

class GetAppUsageStatsUseCase(private val repo : AppRepo)  {
    private val TAG = "GetAppUsageStatsUseCase"

    fun getApps(params: Context): List<App> {
        val appsUsageStats = repo.getAppsProgressInfo(params)
        return setListApp(appsUsageStats, params).sortedBy {
            it.lastTimeUsed
        }

    }

    private fun setListApp(listAppUsage : List<UsageStats>, context : Context) : List<App>{
        val pm = context.packageManager
        val listApplication = mutableListOf<App>()
        for(app in listAppUsage){
            try{
                val appInfo = pm.getApplicationInfo(app.packageName, 0)
                if(app.packageName == context.packageName) continue
                if(appInfo.sourceDir.startsWith("/data/app/")
                    && appInfo.flags != ApplicationInfo.FLAG_SYSTEM
                    && appInfo.flags != ApplicationInfo.FLAG_UPDATED_SYSTEM_APP ){
                    listApplication.add(App(
                        pm.getApplicationIcon(appInfo),
                        pm.getApplicationLabel(appInfo).toString(),
                        appInfo.packageName,
                        app.lastTimeUsed,
                        appInfo.sourceDir))
                }
            }catch(e : PackageManager.NameNotFoundException){

            }

        }

        return listApplication.toList()
    }


}