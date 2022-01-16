package com.example.domain.useCase.broadcast

import javax.inject.Inject

class OffReminderUseCase @Inject constructor(private val broadcastRepositoryCase: BroadcastRepositoryCase) {
    fun offReminder(id: Int) {
        broadcastRepositoryCase.offReminder(id)
    }
}