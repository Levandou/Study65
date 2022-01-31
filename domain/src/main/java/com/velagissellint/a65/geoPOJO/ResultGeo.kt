package com.velagissellint.a65.geoPOJO

import com.google.gson.annotations.SerializedName
import com.velagissellint.a65.geoPOJO.featureMember.FeatureMember

data class ResultGeo(
    @SerializedName("response")
    var response: Response? = null
)

data class Response(
    @SerializedName("GeoObjectCollection")
    var geoObjectCollection: GeoObjectCollection? = null
)

data class GeoObjectCollection(
    @SerializedName("featureMember")
    var featureMember: List<FeatureMember>? = null
)
