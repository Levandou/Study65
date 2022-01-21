package com.velagissellint.a65

import java.util.Calendar
import java.util.GregorianCalendar
const val YEAR = 2000
const val MONTH = Calendar.NOVEMBER
const val DAY = 11
data class DetailedInformationAboutContact(
    val imageResource: String = "",
    val fullName: String = "Сигал Левв",
    val phoneNumber: String = "89199176830",
    val email: String = "velagissellint@gmail.com",
    val description: String = "Хороший работник",
    val birthday: Calendar = GregorianCalendar(YEAR, MONTH, DAY, 0, 0, 0),
    val id: Int? = null
)
