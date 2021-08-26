package com.mendozasolutions.batterysaver.presentation.home


import android.app.usage.UsageStats
import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mendozasolutions.batterysaver.data.model.App
import com.mendozasolutions.batterysaver.domain.repository.home.AppRepo
import com.mendozasolutions.batterysaver.domain.usecase.home.GetAppUsageStatsUseCase
import com.mendozasolutions.batterysaver.domain.usecase.home.KillProcessAppUseCase

class HomeViewModel (private val getAppUsageStatsUseCase : GetAppUsageStatsUseCase) : ViewModel() {
    private val TAG = "HomeViewModel"
   private val listAppRunning : MutableLiveData<List<App>> by lazy {
        MutableLiveData()
    }

    fun fetchAppRunning(context : Context){
        val listApp = getAppUsageStatsUseCase.getApps(context)
        listAppRunning.value = listApp
    }

    fun killAppsProcess(context: Context, listApps: MutableList<App>){

    }

    fun getListAppRunning() : LiveData<List<App>> = listAppRunning



}

class HomeViewModelFactory(private val getAppUsageStatsUseCase : GetAppUsageStatsUseCase) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(GetAppUsageStatsUseCase::class.java).newInstance(getAppUsageStatsUseCase)
    }

}