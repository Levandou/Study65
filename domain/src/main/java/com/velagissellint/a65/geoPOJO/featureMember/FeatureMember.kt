package com.velagissellint.a65.geoPOJO.featureMember

import com.google.gson.annotations.SerializedName
import com.velagissellint.a65.geoPOJO.featureMember.geocoderMetaData.GeocoderMetaData

data class FeatureMember(
    @SerializedName("GeoObject")
    var geoObject: GeoObject? = null
)

data class GeoObject(
    @SerializedName("metaDataProperty")
    var metaDataProperty: MetaDataProperty1? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("Point")
    var point: Point1? = null
)

data class MetaDataProperty1(
    @SerializedName("GeocoderMetaData")
    var geocoderMetaData: GeocoderMetaData? = null
)

data class Point1(
    @SerializedName("pos")
    var pos: String? = null
)
