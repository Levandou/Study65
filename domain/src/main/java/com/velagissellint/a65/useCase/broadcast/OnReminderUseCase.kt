package com.velagissellint.a65.useCase.broadcast

import com.velagissellint.a65.DetailedInformationAboutContact
import javax.inject.Inject

class OnReminderUseCase @Inject constructor(private val broadcastRepositoryCase: BroadcastRepositoryCase) {
    fun onReminder(id: Int, detailedInformationAboutContact: DetailedInformationAboutContact) {
        broadcastRepositoryCase.onReminder(id, detailedInformationAboutContact)
    }
}