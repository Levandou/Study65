package com.velagissellint.a65.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AdministrativeArea(
    @SerializedName("AdministrativeAreaName")
    @Expose
    private val administrativeAreaName: String? = null

    /* @SerializedName("Locality")
     @Expose
     private val locality: Locality? = null
 */
)

