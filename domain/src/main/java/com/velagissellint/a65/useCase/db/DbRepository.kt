package com.velagissellint.a65.useCase.db

import com.velagissellint.a65.geoPOJO.featureMember.geocoderMetaData.GeocoderMetaData

interface DbRepository {
    fun addToDb(geocoderMetaData: GeocoderMetaData)

    fun getFromDb(id: String): GeocoderMetaData

    fun getAllFromDb(id: String): List<GeocoderMetaData>
}
