package com.mendozasolutions.batterysaver.domain.usecase.home

import android.app.usage.UsageStats
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.mendozasolutions.batterysaver.core.Result
import com.mendozasolutions.batterysaver.data.model.App
import com.mendozasolutions.batterysaver.domain.repository.home.AppRepo
import com.mendozasolutions.batterysaver.domain.usecase.UseCase

class GetAppUsageStatsUseCase(private val repo : AppRepo)  {

    fun getApps(params: Context): List<App> {
        val appsUsageStats = repo.getAppsProgressInfo(params)
        return setListApp(appsUsageStats, params)

    }

    private fun setListApp(listAppUsage : List<UsageStats>, context : Context) : List<App>{
        val pm = context.packageManager
        val listApplication = mutableListOf<App>()
        for(app in listAppUsage){
            try{
                val appInfo = pm.getApplicationInfo(app.packageName, 0)
                if(appInfo.sourceDir.startsWith("/data/app/") && appInfo.flags != ApplicationInfo.FLAG_SYSTEM && appInfo.flags != ApplicationInfo.FLAG_UPDATED_SYSTEM_APP ){
                    listApplication.add(App(pm.getApplicationIcon(appInfo), pm.getApplicationLabel(appInfo).toString(), appInfo.packageName, appInfo.sourceDir))
                }
            }catch(e : PackageManager.NameNotFoundException){

            }

        }

        return listApplication.toList()
    }


}