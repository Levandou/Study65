package com.velagissellint.a65

import java.util.Calendar
import java.util.GregorianCalendar

data class DetailedInformationAboutContact(
        val imageResource: String="" ,
        val fullName: String = "Сигал Левв",
        val phoneNumber: String = "89199176830",
        val email: String = "velagissellint@gmail.com",
        val description: String = "Хороший работник",
        val birthday: Calendar = GregorianCalendar(2000, Calendar.NOVEMBER, 11, 0, 0, 0),
        val id:Int?=null
)