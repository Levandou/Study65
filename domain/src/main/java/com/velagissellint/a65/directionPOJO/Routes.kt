package com.velagissellint.a65.directionPOJO

import com.google.gson.annotations.SerializedName

data class Routes(
    @SerializedName("overview_polyline")
    var overviewPolyline: OverviewPolyline? = null
)
