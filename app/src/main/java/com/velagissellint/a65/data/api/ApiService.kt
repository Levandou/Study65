package com.velagissellint.a65.data.api

import com.velagissellint.a65.domain.pojo.Country
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    //@GET("https://geocode-maps.yandex.ru/1.x/?apikey=a1334826-95f7-4ac5-a282-fb0ce6f532ee&format=json&geocode=37.618432,55.758249")


    @GET("1.x/")
    fun getInfoAboutPlace(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = "a1334826-95f7-4ac5-a282-fb0ce6f532ee",
        @Query(QUERY_FORMAT) format: String = "json",
        @Query(QUERY_GEOCODE) geocode: String
    ): Single<Country>

    companion object {
        private const val QUERY_PARAM_API_KEY = "apikey"
        private const val QUERY_GEOCODE = "geocode"
        private const val QUERY_FORMAT = "format"
    }
}