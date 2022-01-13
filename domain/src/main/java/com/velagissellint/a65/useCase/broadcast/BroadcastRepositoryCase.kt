package com.velagissellint.a65.useCase.broadcast

import com.velagissellint.a65.DetailedInformationAboutContact

interface BroadcastRepositoryCase {
    fun offReminder(id: Int)

    fun onReminder(id: Int, detailedInformationAboutContact: DetailedInformationAboutContact)
}