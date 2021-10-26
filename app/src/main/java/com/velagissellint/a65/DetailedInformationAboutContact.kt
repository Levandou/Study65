package com.velagissellint.a65

import androidx.annotation.DrawableRes

data class DetailedInformationAboutContact(
        @DrawableRes val imageResource: Int = R.drawable.emp,
        val fullName: String = "Сигал Левв",
        val phoneNumber: String = "89199176830",
        val email: String = "velagissellint@gmail.com",
        val description: String = "Хороший работник"
)