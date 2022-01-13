package com.example.domain.useCase.contactDetails

import com.example.domain.DetailedInformationAboutContact
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface ContactDetailsRepositoryCase {
    fun getContact(id:Int): Single<DetailedInformationAboutContact>?

}