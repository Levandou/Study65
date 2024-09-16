package com.velagissellint.a65.useCase.geo

import com.velagissellint.a65.geoPOJO.ResultGeo
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetGeoUseCase @Inject constructor(private val geoRepository: GeoRepository) {
    fun getGeo(geo: String): Single<ResultGeo> {
        return geoRepository.getGeo(geo)
    }
}
