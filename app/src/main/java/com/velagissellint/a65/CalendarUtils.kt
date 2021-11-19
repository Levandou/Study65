package com.velagissellint.a65

import java.util.Calendar
import java.util.GregorianCalendar

fun putNextBirthday(calendar: Calendar): Long {
    val calendarNext = calendar as GregorianCalendar
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    calendarNext[Calendar.SECOND] = 0
    calendarNext[Calendar.MINUTE] = 0
    calendarNext[Calendar.HOUR] = 0
    calendarNext[Calendar.DATE] = calendar[Calendar.DATE]
    calendarNext[Calendar.MONTH] = calendar[Calendar.MONTH]
    calendarNext[Calendar.YEAR] = calendar[Calendar.YEAR]

    if (!isFebruary29(calendar)) {
        if (calendar[Calendar.DAY_OF_YEAR] > Calendar.getInstance()[Calendar.DAY_OF_YEAR]) {
            calendarNext[Calendar.YEAR] = currentYear
        } else {
            calendarNext[Calendar.YEAR] = currentYear + 1
        }
    }

    if (isFebruary29(calendar)) {
        if (calendar[Calendar.DAY_OF_YEAR] > Calendar.getInstance()[Calendar.DAY_OF_YEAR]) {
            calendarNext[Calendar.YEAR] = currentYear
            while (!calendarNext.isLeapYear(calendarNext[Calendar.YEAR])) {
                ++calendarNext[Calendar.YEAR]
            }
        } else {
            calendarNext[Calendar.YEAR] = currentYear + 1
            while (!calendarNext.isLeapYear(calendarNext[Calendar.YEAR])) {
                ++calendarNext[Calendar.YEAR]
            }
        }
    }

    return calendarNext.timeInMillis
}

private fun isFebruary29(calendar: Calendar) =
    calendar[Calendar.MONTH] == Calendar.FEBRUARY && calendar[Calendar.DATE] == 29