package com.velagissellint.a65.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Country (
     @SerializedName("AddressLine")
     @Expose
     var addressLine: String? = null,

     @SerializedName("CountryNameCode")
     @Expose
     var countryNameCode: String? = null,

     @SerializedName("CountryName")
     @Expose
     var countryName: String? = null,

      @SerializedName("AdministrativeArea")
     @Expose
     var administrativeArea: AdministrativeArea? = null,
)