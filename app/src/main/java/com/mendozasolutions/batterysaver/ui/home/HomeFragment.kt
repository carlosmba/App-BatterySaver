package com.mendozasolutions.batterysaver.ui.home

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mendozasolutions.batterysaver.R
import com.mendozasolutions.batterysaver.data.model.App
import com.mendozasolutions.batterysaver.databinding.HomeFragmentBinding
import com.mendozasolutions.batterysaver.data.repository.AppRepoImpl
import com.mendozasolutions.batterysaver.domain.usecase.home.GetAppUsageStatsUseCase
import com.mendozasolutions.batterysaver.domain.usecase.home.KillProcessAppUseCase
import com.mendozasolutions.batterysaver.presentation.home.HomeViewModel
import com.mendozasolutions.batterysaver.presentation.home.HomeViewModelFactory
import com.mendozasolutions.batterysaver.ui.home.adapter.HomeAdapter

class HomeFragment : Fragment(), OnClickAppListener {
    private val TAG = "HomeFragment"
    private var _binding : HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var mAdapter : HomeAdapter
    private val repo : AppRepoImpl by lazy { AppRepoImpl() }
    private val viewModel : HomeViewModel by activityViewModels(){
        HomeViewModelFactory(GetAppUsageStatsUseCase(repo))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = HomeAdapter(requireActivity(), this)
        if(checkUsageStatsPermission()){
            activity?.let { viewModel.fetchAppRunning(it) }
        }else{
            requireActivity().startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }

        binding.btnStop.setOnClickListener { onClickForceCloseApp(mAdapter.getListApp()) }
        binding.checkBoxAll.setOnCheckedChangeListener { _, isChecked ->
            onClickCheckBoxAll(isChecked)

        }
        setupViewModel()
        setupRecyclerView()

    }

    @SuppressLint("SetTextI18n")
    private fun setupViewModel(){
        viewModel.getListAppRunning().observe(viewLifecycleOwner){
            mAdapter.setListApp(it)
            binding.btnStop.text = getString(R.string.btn_text_stop) + " " + it.size + " apps"
        }
    }

    private fun setupRecyclerView(){

        binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun checkUsageStatsPermission() : Boolean{
        val appOpsManager = requireActivity().getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), requireActivity().packageName)
        return mode == AppOpsManager.MODE_ALLOWED
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    override fun onClickForceCloseApp(listApp : List<App>) {
        try{
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${listApp[1].packageName}"))
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
        }catch(e : ActivityNotFoundException){
            e.printStackTrace()
        }


    /*listApp.forEach { app ->
            if(app.isCheck){
                try{
                    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${app.packageName}")))

                }catch(e : ActivityNotFoundException){
                    e.printStackTrace()
                }
            }

        }*/

    }

    override fun onClickCheckBoxAll(isCheck : Boolean) {
        mAdapter.toggleCheckBoxAll(isCheck)
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "Metodo onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "Metodo onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "Metodo onPause()")
    }


}