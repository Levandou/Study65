package com.velagissellint.a65.useCase.geo

import com.velagissellint.a65.directionPOJO.ResultDirection
import com.velagissellint.a65.geoPOJO.ResultGeo
import io.reactivex.rxjava3.core.Single

interface GeoRepository {
    fun getGeo(geo: String): Single<ResultGeo>

    fun getDirection(url: String): Single<ResultDirection>
}
