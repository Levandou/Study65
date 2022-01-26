package tests

import com.velagissellint.a65.all.data.putNextBirthday
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.util.Calendar

class Test : DescribeSpec() {
    val calendarNow = Calendar.getInstance()
    val notidicationCalendar = Calendar.getInstance()

    init {
        describe("Тестирование поиска даты для установки напоминания") {
            it("(1)У контакта в этом году прошёл день рождение, дата уведомления должна быть в следущем году") {
                calendarNow.set(1999, 8, 9)
                notidicationCalendar.set(2000, 8, 8)
                val birthdayOfContactCalendar = Calendar.getInstance()
                birthdayOfContactCalendar
                    .set(1996, 8, 8)
                val notidicationCalendarFromMethod = putNextBirthday(
                    birthdayOfContactCalendar, calendarNow
                )
                checkOfMethod(notidicationCalendar, notidicationCalendarFromMethod)
            }

            it("(2)У контакта в этом году ещё не было дня рождения, дата уведомления должна быть в текущем году") {
                calendarNow.set(1999, 8, 7)
                notidicationCalendar.set(1999, 8, 8)
                val birthdayOfContactCalendar = Calendar.getInstance()
                birthdayOfContactCalendar
                    .set(1996, 8, 8)
                val notidicationCalendarFromMethod = putNextBirthday(
                    birthdayOfContactCalendar, calendarNow
                )
                checkOfMethod(notidicationCalendar, notidicationCalendarFromMethod)
            }

            it(
                "(3)День рождения контакта в високосном году 29 февраля," +
                        "дата напоминания должна быть в следущем году т.к." +
                        " следущий год високосный"
            ) {
                calendarNow.set(1999, 2, 2)
                notidicationCalendar.set(2000, 1, 29)
                val birthdayOfContactCalendar = Calendar.getInstance()
                birthdayOfContactCalendar
                    .set(1996, 1, 29)
                val notidicationCalendarFromMethod = putNextBirthday(
                    birthdayOfContactCalendar, calendarNow
                )
                checkOfMethod(notidicationCalendar, notidicationCalendarFromMethod)
            }

            it(
                "(4)День рождения контакта 29 февраля, в этом году был день рождения, " +
                        "по заданию если у контакта день рождения в високосный год," +
                        " то напоминание должно быть поставлена на следущий високосный год"
            ) {
                calendarNow.set(2000, 2, 1)
                notidicationCalendar.set(2004, 1, 29)
                val birthdayOfContactCalendar = Calendar.getInstance()
                birthdayOfContactCalendar
                    .set(1996, 1, 29)
                val notidicationCalendarFromMethod = putNextBirthday(
                    birthdayOfContactCalendar, calendarNow
                )
                checkOfMethod(notidicationCalendar, notidicationCalendarFromMethod)
            }
        }
    }

    private fun checkOfMethod(
        notidicationCalendar: Calendar,
        notidicationCalendarFromMethod: Calendar
    ) {
        notidicationCalendarFromMethod.get(Calendar.DAY_OF_MONTH) shouldBe notidicationCalendar.get(
            Calendar.DAY_OF_MONTH
        )
        notidicationCalendarFromMethod.get(Calendar.MONTH) shouldBe notidicationCalendar.get(
            Calendar.MONTH
        )
        notidicationCalendarFromMethod.get(Calendar.YEAR) shouldBe notidicationCalendar.get(Calendar.YEAR)
    }
}
