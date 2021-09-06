package com.mendozasolutions.batterysaver.ui.battery

import android.content.*
import android.os.BatteryManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mendozasolutions.batterysaver.R
import com.mendozasolutions.batterysaver.databinding.BatteryFragmentBinding
import java.text.DecimalFormat

class BatteryFragment : Fragment() {
    private var _binding : BatteryFragmentBinding? = null
    private val binding get() = _binding!!
    private var batteryReceiver : BatteryReceiver? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BatteryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        batteryReceiver = BatteryReceiver()

    }



    override fun onDestroyView() {
        _binding = null
        batteryReceiver = null
        super.onDestroyView()
    }

    override fun onStart() {
        super.onStart()
        activity?.registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onStop() {
        activity?.unregisterReceiver(batteryReceiver)
        super.onStop()

    }

    private inner class BatteryReceiver() : BroadcastReceiver(){
        private val TAG = "BatteryReceiver"

        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null && intent.action == Intent.ACTION_BATTERY_CHANGED) {
                val prc = 100 * intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 1) /
                        intent.getIntExtra(BatteryManager.EXTRA_SCALE, 1)
                binding.progressBar.progress = prc
                binding.tvProgress.text = prc.toString()

                val temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0).toFloat() / 10
                binding.tvTemp.text = getString(R.string.text_temperature_battery, DecimalFormat("#.00").format(temp))

                val state = when (intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)) {
                    BatteryManager.BATTERY_HEALTH_GOOD -> getString(R.string.text_health_good)
                    BatteryManager.BATTERY_HEALTH_DEAD -> getString(R.string.text_health_dead)
                    BatteryManager.BATTERY_HEALTH_OVERHEAT -> getString(R.string.text_health_overheat)
                    else -> getString(R.string.text_health_unknown)
                }
                binding.tvStateBattery.text = getString(R.string.text_state_battery, state)

                val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                val charge =
                    if(status == BatteryManager.BATTERY_STATUS_FULL ||
                        status == BatteryManager.BATTERY_STATUS_CHARGING) getString(R.string.text_charging_battery)
                    else getString(R.string.text_no_charging_battery)

                binding.tvStateCharge.text = getString(R.string.text_state_charge_Battery, charge)

               /* val plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
                battery!!.isAcCharge = plugged == BatteryManager.BATTERY_PLUGGED_AC
                battery!!.isUsbCharge = plugged == BatteryManager.BATTERY_PLUGGED_USB*/

            }
        }
    }


}