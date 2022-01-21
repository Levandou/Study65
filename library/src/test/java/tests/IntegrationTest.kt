package tests

import com.velagissellint.a65.DetailedInformationAboutContact
import com.velagissellint.a65.all.presentation.contactDetails.ContactDetailsViewModel
import com.velagissellint.a65.useCase.broadcast.BroadcastRepositoryCase
import com.velagissellint.a65.useCase.broadcast.OffReminderUseCase
import com.velagissellint.a65.useCase.broadcast.OnReminderUseCase
import com.velagissellint.a65.useCase.contactDetails.GetContactDetailsUseCase
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import java.util.GregorianCalendar

class IntegrationTest : DescribeSpec() {
    @MockK
    private lateinit var broadcastRepositoryCase: BroadcastRepositoryCase

    @MockK
    lateinit var getContactDetailsUseCase: GetContactDetailsUseCase
    var offReminderUseCase: OffReminderUseCase
    var onReminderUseCase: OnReminderUseCase
    private var viewmodel: ContactDetailsViewModel

    init {
        MockKAnnotations.init(this)
        offReminderUseCase = OffReminderUseCase(broadcastRepositoryCase)
        onReminderUseCase = OnReminderUseCase(broadcastRepositoryCase)
        viewmodel =
            ContactDetailsViewModel(getContactDetailsUseCase, offReminderUseCase, onReminderUseCase)
        describe("Включение и выключение установки напоминания о дне рождения") {
            it("(1)выключение установи напоминания") {
                val detailedInformationAboutContact = DetailedInformationAboutContact(
                    fullName = "Иванов Иван",
                    birthday = GregorianCalendar(2000, 11, 16, 0, 0, 0),
                    id = 1
                )
                every {
                    detailedInformationAboutContact.id?.let {
                        broadcastRepositoryCase.offReminder(it)
                    }
                } returns Unit
                detailedInformationAboutContact.id?.let { viewmodel.offReciver(it) } shouldBe Unit
                viewmodel.isAlarmSet shouldBe false
            }

            it("(2)включение установки напоминания") {
                val detailedInformationAboutContact = DetailedInformationAboutContact(
                    fullName = "Иванов Иван",
                    birthday = GregorianCalendar(2000, 11, 16, 0, 0, 0),
                    id = 1
                )
                every {
                    detailedInformationAboutContact.id?.let {
                        broadcastRepositoryCase.onReminder(it, detailedInformationAboutContact)
                    }
                } returns Unit
                detailedInformationAboutContact.id?.let {
                    viewmodel.onReciver(
                        it,
                        detailedInformationAboutContact
                    )
                } shouldBe Unit
                viewmodel.isAlarmSet shouldBe true
            }
        }
    }
}
