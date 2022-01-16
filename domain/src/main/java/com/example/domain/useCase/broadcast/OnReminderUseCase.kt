package com.example.domain.useCase.broadcast

import com.example.domain.DetailedInformationAboutContact
import javax.inject.Inject

class OnReminderUseCase @Inject constructor(private val broadcastRepositoryCase: BroadcastRepositoryCase) {
    fun onReminder(id: Int, detailedInformationAboutContact: DetailedInformationAboutContact) {
        broadcastRepositoryCase.onReminder(id, detailedInformationAboutContact)
    }
}