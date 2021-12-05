package com.velagissellint.a65.all.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.velagissellint.a65.geoPOJO.featureMember.geocoderMetaData.GeocoderMetaData

@Database(entities = [GeocoderMetaData::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DB_NAME = "main.db"
    }

    abstract fun resultDao(): GeoDao
}
