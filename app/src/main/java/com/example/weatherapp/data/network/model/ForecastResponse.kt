package com.example.weatherapp.data.network.model

import com.google.gson.annotations.SerializedName

data class ForecastResponse(

    @SerializedName("city")
    val city: City?,
    @SerializedName("cnt")
    val cnt: Int?,
    @SerializedName("cod")
    val cod: String?,
    @SerializedName("list")
    val list: List<ListItem>?,
    @SerializedName("message")
    val message: Int?
)

/*fun ForecastResponse.asExternalModel():Forecast{
    return Forecast(city?.name.toString(),0322.5)
}
*/