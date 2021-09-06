package com.mendozasolutions.batterysaver.domain.usecase.main

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import com.mendozasolutions.batterysaver.data.model.App
import com.mendozasolutions.batterysaver.domain.services.ConfigHibernationProcess
import kotlinx.coroutines.*

class HibernationProcessUseCase {

    private val TAG = "HibernationProcessUseCase"
    private var context : Context? = null
    private val listAppChecked = mutableListOf<App>()
    private var size :Int = 0


    fun initHibernation(contextMain : Context, listApps :List<App> ){
        context = contextMain
        listApps.forEach {app ->
            if(app.isCheck) listAppChecked.add(app)
        }
        size = listAppChecked.size - 1
        ConfigHibernationProcess.isHibernation = true
        hibernationProcess()

    }
    fun continueHibernation() = hibernationProcess()

    private fun hibernationProcess(){
        if(size >= 0){
            launchHibernationApp(listAppChecked[size])
            size -= 1
        }else{
            ConfigHibernationProcess.isHibernation = false
        }

    }

    private fun launchHibernationApp(app : App){
        try{
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${app.packageName}"))
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            context?.startActivity(intent)
        }catch(e : ActivityNotFoundException){
            e.printStackTrace()
        }

    }

}