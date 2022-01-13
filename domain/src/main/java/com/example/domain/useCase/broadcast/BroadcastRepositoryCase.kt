package com.example.domain.useCase.broadcast

import com.example.domain.DetailedInformationAboutContact

interface BroadcastRepositoryCase {
    fun offReminder(id:Int)

    fun onReminder(id:Int,detailedInformationAboutContact: DetailedInformationAboutContact)
}