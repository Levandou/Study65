package com.velagissellint.a65.all.data

import java.util.Calendar
import java.util.GregorianCalendar

fun putNextBirthday(calendar: Calendar?, calendarNow: Calendar): Calendar {
    val calendarNext = calendar as GregorianCalendar
    val currentYear = calendarNow.get(Calendar.YEAR)
    calendarNext[Calendar.SECOND] = 0
    calendarNext[Calendar.MINUTE] = 0
    calendarNext[Calendar.HOUR] = 0
    calendarNext[Calendar.DATE] = calendar[Calendar.DATE]
    calendarNext[Calendar.MONTH] = calendar[Calendar.MONTH]
    calendarNext[Calendar.YEAR] = calendar[Calendar.YEAR]

    if (!isFebruary29(calendar)) {
        if (calendar[Calendar.DAY_OF_YEAR] > calendarNow[Calendar.DAY_OF_YEAR]) {
            calendarNext[Calendar.YEAR] = currentYear
        } else {
            calendarNext[Calendar.YEAR] = currentYear + 1
        }
    }

    if (isFebruary29(calendar)) {
        if (calendar[Calendar.DAY_OF_YEAR] >= calendarNow[Calendar.DAY_OF_YEAR]) {

            calendarNext[Calendar.YEAR] = currentYear
            while (!calendarNext.isLeapYear(calendarNext[Calendar.YEAR])) {
                ++calendarNext[Calendar.YEAR]
            }
            calendarNext[Calendar.MONTH] = Calendar.FEBRUARY
            calendarNext[Calendar.DAY_OF_MONTH] = TWENTY_NINE
        } else {
            calendar[Calendar.MONTH] = Calendar.FEBRUARY
            calendar[Calendar.DAY_OF_MONTH] = TWENTY_NINE

            calendarNext[Calendar.YEAR] = currentYear + 1
            while (!calendarNext.isLeapYear(calendarNext[Calendar.YEAR])) {
                ++calendarNext[Calendar.YEAR]
            }
            calendarNext[Calendar.MONTH] = Calendar.FEBRUARY
            calendarNext[Calendar.DAY_OF_MONTH] = TWENTY_NINE
        }
    }
    return calendarNext
}

const val TWENTY_NINE = 29

private fun isFebruary29(calendar: Calendar) =
    calendar[Calendar.MONTH] == Calendar.FEBRUARY && calendar[Calendar.DATE] == TWENTY_NINE
