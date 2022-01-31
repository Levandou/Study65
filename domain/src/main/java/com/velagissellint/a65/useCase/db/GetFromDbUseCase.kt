package com.velagissellint.a65.useCase.db

import com.velagissellint.a65.geoPOJO.featureMember.geocoderMetaData.GeocoderMetaData
import javax.inject.Inject

class GetFromDbUseCase @Inject constructor(private val dbRepository: DbRepository) {
    fun getFromDb(id: String): GeocoderMetaData = dbRepository.getFromDb(id)

    fun getAllFromDb(id: String): List<GeocoderMetaData> = dbRepository.getAllFromDb(id)

    fun addToDb(geocoderMetaData: GeocoderMetaData) {
        dbRepository.addToDb(geocoderMetaData)
    }
}
