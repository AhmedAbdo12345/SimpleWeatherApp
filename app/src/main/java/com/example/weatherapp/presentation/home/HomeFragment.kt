package com.example.weatherapp.presentation.home


import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Address
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.Params.CELSIUS
import com.example.weatherapp.Params.CITY
import com.example.weatherapp.Params.LANGUAGE
import com.example.weatherapp.Params.LATITUDE
import com.example.weatherapp.Params.LOCATION
import com.example.weatherapp.Params.LONGITUDE
import com.example.weatherapp.Params.NETWORK_ERROR_MESSAGE
import com.example.weatherapp.Params.TEMPERATURE
import com.example.weatherapp.R
import com.example.weatherapp.data.network.model.ForecastResponse
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.utils.ConvertUnits
import com.example.weatherapp.utils.CurrentLocation
import com.example.weatherapp.utils.CurrentLocationStatue
import com.example.weatherapp.utils.IconsApp
import com.example.weatherapp.utils.UiState
import com.example.weatherapp.utils.permissionId
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class HomeFragment : Fragment(), CurrentLocationStatue {


    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel by viewModels<WeatherViewModel> {
        WeatherViewModel.Factory
    }
    private lateinit var currentLocation: CurrentLocation
    private lateinit var sharedPreference: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var currentLanguage: String = "en"
    private var tempUnit = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference =
            requireActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        editor = sharedPreference.edit()


        currentLanguage = sharedPreference.getString(LANGUAGE, "en").toString()
        checkLanguage(currentLanguage)

        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.app_name)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //  return inflater.inflate(R.layout.fragment_home, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tempUnit = sharedPreference.getString(TEMPERATURE, CELSIUS).toString()

        currentLocation =
            CurrentLocation(requireContext(), requireActivity(), this, currentLanguage)
        currentLocation.getLocation()


        binding.swipeRefreshLayout.setOnRefreshListener {
            currentLocation =
                CurrentLocation(requireContext(), requireActivity(), this, currentLanguage)
            currentLocation.getLocation()
            binding.swipeRefreshLayout.isRefreshing = false;
        }
    }

    private fun getDateTime(
        dt: Int,
        simpleDateFormat: SimpleDateFormat = SimpleDateFormat("E,d MMM ',' hh:mm aa")
    ): String? {
        // val cityTxtFormat = SimpleDateFormat("E,d MMM ',' hh:mm aa")

        val cityTxtData = Date(dt.toLong() * 1000)
        return simpleDateFormat.format(cityTxtData)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionId) {

            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                currentLocation.getLocation()

            }
        }}

    override fun success(list: List<Address>) {
        list.let {
            getDataFromNetwork(
                list[0].latitude,
                list[0].longitude,
                currentLanguage,
                list[0].adminArea
            )

            editor.putString(LOCATION, "GPS")
            editor.putString(CITY, list[0].adminArea)
            editor.putString(LATITUDE, (list[0].latitude).toString())
            editor.putString(LONGITUDE, (list[0].longitude).toString())
            editor.commit()
        }


    }


    private fun getDataFromNetwork(lat: Double, lon: Double, lang: String, city: String) {

        homeViewModel.getWeather(lat, lon, lang)

        lifecycleScope.launch {
            homeViewModel.weather.collect {

                when (it) {
                    is UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.nestedScrollView.visibility = View.GONE
                        binding.layoutNetworkError.visibility = View.GONE
                        binding.imgErrorPageNotFound.visibility = View.GONE

                        Log.d("zxcvb", "getDataFromNetwork: KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK")
                    }

                    is UiState.Success -> {
                        binding.nestedScrollView.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        binding.layoutNetworkError.visibility = View.GONE
                        binding.imgErrorPageNotFound.visibility = View.GONE

                        displayDailyRecycleView(it.data)
                        displayWeatherInfo(it.data, city)
                        Log.d("zxcvb", "getDataFromNetwork: LLLLLLLLLLLLLLLLLLLLLLLLLLLLLL")

                    }

                    is UiState.Failed -> {
                        binding.nestedScrollView.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        if (it.message == NETWORK_ERROR_MESSAGE) {
                            binding.layoutNetworkError.visibility = View.VISIBLE
                        } else {
                            binding.imgErrorPageNotFound.visibility = View.VISIBLE

                        }
                        Log.d("zxcvb", "getDataFromNetwork: XXXXXXXXXXXXXXXXXXXXXXXx" + it.message)

                    }

                }

            }
        }
    }


    private fun displayDailyRecycleView(forecastResponse: ForecastResponse) {
        val dailyAdapter = HomeAdapter(forecastResponse, tempUnit)
        binding.rvDay.apply {
            adapter = dailyAdapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 1).apply {
                orientation = RecyclerView.VERTICAL
            }
        }
    }

    private fun displayWeatherInfo(forecastResponse: ForecastResponse, city: String) {
        val forecast = forecastResponse.list?.get(0)
        forecast?.let { listItem ->
            binding.tvCity.text = city
            val format = SimpleDateFormat("E,d MMM ',' hh:mm aa")
            val date = Date()
            val currentTimeAndDate = format.format(date)
            binding.tvDate.text = currentTimeAndDate
            IconsApp.getSuitableIcon(
                listItem.weather?.get(0)?.icon ?: "03d",
                binding.imgStatusWeather
            )
            binding.tvCurrentTemp.text =
                ConvertUnits.convertTemp(forecast.main?.temp as Double, tempUnit = tempUnit)

            binding.tvDescription.text = listItem.weather?.get(0)?.description ?: "not found"
            //---------------------------------------------------------------------------------------

            binding.tvPressure.text = "${listItem.main?.pressure} hpa"
            binding.tvHumidity.text = "${listItem.main?.humidity} %"
            binding.tvWind.text = "${listItem.wind?.speed} m/s"

            binding.tvCloud.text = "${listItem.clouds?.all} %"
            binding.tvVisibility.text = "${listItem.visibility} m"

            binding.tvTemperature.text =
                ConvertUnits.convertTemp(forecast.main.temp, tempUnit = tempUnit)


            binding.tvSunRise.text =
                forecastResponse.city?.sunrise?.let {
                    getDateTime(
                        it,
                        SimpleDateFormat("hh:mm aa")
                    )
                }

            binding.tvSunSet.text =
                forecastResponse.city?.sunset?.let { getDateTime(it, SimpleDateFormat("hh:mm aa")) }
        }


    }

    private fun checkLanguage(language: String) {

        val locale = Locale(language)

        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        this.resources.updateConfiguration(config, this.resources.displayMetrics)
    }


}
