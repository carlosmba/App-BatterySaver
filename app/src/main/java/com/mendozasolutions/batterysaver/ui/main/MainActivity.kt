package com.mendozasolutions.batterysaver.ui.main

import android.app.ActivityManager
import android.app.AppOpsManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.mendozasolutions.batterysaver.R
import com.mendozasolutions.batterysaver.data.model.App
import com.mendozasolutions.batterysaver.data.repository.AppRepoImpl
import com.mendozasolutions.batterysaver.databinding.ActivityMainBinding
import com.mendozasolutions.batterysaver.domain.services.ConfigHibernationProcess
import com.mendozasolutions.batterysaver.domain.usecase.main.GetAppUsageStatsUseCase
import com.mendozasolutions.batterysaver.domain.usecase.main.HibernationProcessUseCase
import com.mendozasolutions.batterysaver.presentation.main.MainViewModel
import com.mendozasolutions.batterysaver.presentation.main.MainViewModelFactory
import com.mendozasolutions.batterysaver.ui.battery.BatteryFragment
import com.mendozasolutions.batterysaver.ui.main.adapter.MainAdapter

class MainActivity : AppCompatActivity(), OnClickAppListener {
    private lateinit var binding : ActivityMainBinding
    private val TAG = "MainActivity"
    private lateinit var mAdapter : MainAdapter
    private var listApp : MutableList<App> = mutableListOf()
    private var sizeListApp : Int = 0
    private val viewModel : MainViewModel by viewModels(){
        MainViewModelFactory(GetAppUsageStatsUseCase(AppRepoImpl()), HibernationProcessUseCase())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAdapter = MainAdapter(this)
        if(checkUsageStatsPermission()) viewModel.fetchAppRunning(this)
        else startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))


        binding.btnStop.setOnClickListener{ viewModel.hibernationProcess(this, mAdapter.getListApp()) }
        binding.checkBoxAll.setOnCheckedChangeListener { _, isChecked ->
            onClickCheckBoxAll(isChecked)

        }
        setupBatteryFragment()
        setupViewModel()
        setupRecyclerView()
    }


    private fun setupViewModel(){
        viewModel.getListAppRunning().observe(this){ list ->
            mAdapter.setListApp(list)
            sizeListApp = list.size - 1
        }
        viewModel.getSelectedAppSize().observe(this){
            binding.btnStop.text = getString(R.string.btn_text_stop, it.toString())
        }
    }

    private fun setupRecyclerView(){

        binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun checkUsageStatsPermission() : Boolean{
        val appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), packageName)
        return mode == AppOpsManager.MODE_ALLOWED
    }

    override fun onClickCheckBoxAll(isCheck : Boolean) {
        mAdapter.toggleCheckBoxAll(isCheck)
        viewModel.updateCheckBoxAll(isCheck)
    }

    override fun onClickCheckBoxApp(isChecked: Boolean) {
        if(isChecked) viewModel.addOneSelectedApp() else viewModel.subtractOneSelectedApp()
    }

    fun setupBatteryFragment(){
        val fm = supportFragmentManager
     fm.beginTransaction().add(R.id.containerBatteryFragment, BatteryFragment(), BatteryFragment::class.java.name).commit()
    }

    override fun onResume() {
        super.onResume()
        if(ConfigHibernationProcess.isHibernation) viewModel.continueHibernationProcess()
    }



}