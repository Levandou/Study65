package com.velagissellint.a65.all.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.velagissellint.a65.geoPOJO.featureMember.geocoderMetaData.GeocoderMetaData

@Dao
interface GeoDao {
    @androidx.room.Query("SELECT * FROM adres_of_point WHERE idOfContact == :id LIMIT 1")
    fun getInfoAboutPoint(id: String): GeocoderMetaData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInfoAboutPoint(geocoderMetaData: GeocoderMetaData)

    @androidx.room.Query("SELECT * FROM adres_of_point WHERE idOfContact != :id")
    fun getInfoAboutAllPoints(id: String): List<GeocoderMetaData>
}
