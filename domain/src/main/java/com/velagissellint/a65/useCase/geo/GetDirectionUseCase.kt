package com.velagissellint.a65.useCase.geo

import com.velagissellint.a65.directionPOJO.ResultDirection
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetDirectionUseCase @Inject constructor(private val geoRepository: GeoRepository) {
    fun getDirection(url: String): Single<ResultDirection> {
        return geoRepository.getDirection(url)
    }
}
