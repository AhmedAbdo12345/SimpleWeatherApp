package com.example.weatherapp.presentation.setting

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.weatherapp.utils.Params.CELSIUS
import com.example.weatherapp.utils.Params.FAHRENHEIT
import com.example.weatherapp.utils.Params.KELVIN
import com.example.weatherapp.utils.Params.LANGUAGE
import com.example.weatherapp.utils.Params.TEMPERATURE
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding

    private var tempUnit=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //     return inflater.inflate(R.layout.fragment_setting, container, false)

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_setting, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreference =
            requireActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()

        //--------------------------------------------------------------------------------------
        val lastLanguageType= sharedPreference.getString(LANGUAGE,"en")
        if (lastLanguageType.equals("en")){
            binding.radioGroupLanguage.check(binding.appCompatRadioBtnEnglish.id)
        }else{
            binding.radioGroupLanguage.check(binding.appCompatRadioBtnArabic.id)
        }
        //--------------------------------------------------------------------------------------
         tempUnit= sharedPreference.getString(TEMPERATURE,CELSIUS).toString()
        Log.i("zxcv", "temp: $tempUnit")
        when (tempUnit) {
            CELSIUS -> binding.radioGroupTemperature.check(binding.appCompatRadioBtnCelsius.id)
            KELVIN -> binding.radioGroupTemperature.check(binding.appCompatRadioBtnKelvin.id)
           FAHRENHEIT -> binding.radioGroupTemperature.check(binding.appCompatRadioBtnFahrenheit.id)
        else -> { }
        }
        //--------------------------------------------------------------------------------------

        binding.layoutLanguageTitle.setOnClickListener() {
            if (binding.layoutLanguage.isVisible) {
                binding.layoutLanguage.visibility = View.GONE
            } else {
                binding.layoutLanguage.visibility = View.VISIBLE
            }
        }



        binding.layoutTemperatureTitle.setOnClickListener() {
            if (binding.layoutTemperature.isVisible) {
                binding.layoutTemperature.visibility = View.GONE
            } else {
                binding.layoutTemperature.visibility = View.VISIBLE
            }
        }

        binding.radioGroupLanguage.setOnCheckedChangeListener { _, checkedId ->
            val language = if (R.id.appCompatRadioBtn_Arabic == checkedId) "ar" else "en"
            editor.putString(LANGUAGE, language)
            editor.apply()

            NavHostFragment.findNavController(this@SettingFragment)
                .navigate(R.id.action_settingFragment_to_homeFragment)

        }


        binding.radioGroupTemperature.setOnCheckedChangeListener { _, checkedId ->

            when(checkedId){
                R.id.appCompatRadioBtn_Celsius -> tempUnit = CELSIUS
                R.id.appCompatRadioBtn_Kelvin -> tempUnit = KELVIN
                R.id.appCompatRadioBtn_Fahrenheit -> tempUnit = FAHRENHEIT
                else -> {

                }
            }
           editor.putString(TEMPERATURE, tempUnit)
            editor.commit()

           NavHostFragment.findNavController(this@SettingFragment)
                .navigate(R.id.action_settingFragment_to_homeFragment)
        }
    }
}