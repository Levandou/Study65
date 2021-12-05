package com.velagissellint.a65.geoPOJO.featureMember.geocoderMetaData

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "adres_of_point")
data class GeocoderMetaData(

    var location: String,

    @PrimaryKey
    var idOfContact: String,

    @SerializedName("precision")
    var precision: String? = null,

    @SerializedName("text")
    var text: String? = null,

    @SerializedName("kind")
    var kind: String? = null
)
