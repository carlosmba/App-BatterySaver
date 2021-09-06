package com.mendozasolutions.batterysaver.presentation.main


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mendozasolutions.batterysaver.data.model.App
import com.mendozasolutions.batterysaver.domain.usecase.main.GetAppUsageStatsUseCase
import com.mendozasolutions.batterysaver.domain.usecase.main.HibernationProcessUseCase

class MainViewModel (private val getAppUsageStatsUseCase : GetAppUsageStatsUseCase,
                     private val hibernationProcessUseCase : HibernationProcessUseCase) : ViewModel() {
    private val TAG = "HomeViewModel"
    private val selectedAppSize : MutableLiveData<Int> = MutableLiveData()
   private val listAppRunning : MutableLiveData<List<App>> by lazy {
        MutableLiveData()
    }

    fun fetchAppRunning(context : Context){
        val listApp = getAppUsageStatsUseCase.getApps(context)
        listAppRunning.value = listApp
        selectedAppSize.value = listApp.size
    }

    fun hibernationProcess(context : Context, listApp : List<App>){
        hibernationProcessUseCase.initHibernation(context, listApp)
    }

    fun continueHibernationProcess()= hibernationProcessUseCase.continueHibernation()



    fun getListAppRunning() : LiveData<List<App>> = listAppRunning

    fun getSelectedAppSize() : LiveData<Int> = selectedAppSize

    fun subtractOneSelectedApp(){
        selectedAppSize.value = selectedAppSize.value?.minus(1)
    }

    fun addOneSelectedApp(){
        selectedAppSize.value = selectedAppSize.value?.plus(1)
    }

    fun updateCheckBoxAll(isChecked: Boolean){
        if(isChecked) selectedAppSize.value = listAppRunning.value?.size else selectedAppSize.value = 0
    }



}

class MainViewModelFactory(private val getAppUsageStatsUseCase : GetAppUsageStatsUseCase,
                            private val hibernationProcessUseCase : HibernationProcessUseCase) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(GetAppUsageStatsUseCase::class.java, HibernationProcessUseCase::class.java).newInstance(getAppUsageStatsUseCase, hibernationProcessUseCase)
    }

}