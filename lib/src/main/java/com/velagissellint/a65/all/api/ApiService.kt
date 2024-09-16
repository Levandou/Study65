package com.velagissellint.a65.all.api

import com.velagissellint.a65.directionPOJO.ResultDirection
import com.velagissellint.a65.geoPOJO.ResultGeo
import com.velagissellint.a655.BuildConfig
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    @GET("/1.x/")
    fun getInfoAboutPlace(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = BuildConfig.API_KEY_YANDEX,
        @Query(QUERY_FORMAT) format: String = "json",
        @Query(QUERY_GEOCODE) geocode: String
    ): Single<ResultGeo>

    @GET()
    fun getDirection(
        @Url() url: String?
    ): Single<ResultDirection>

    companion object {
        private const val QUERY_PARAM_API_KEY = "apikey"
        private const val QUERY_GEOCODE = "geocode"
        private const val QUERY_FORMAT = "format"
    }
}
