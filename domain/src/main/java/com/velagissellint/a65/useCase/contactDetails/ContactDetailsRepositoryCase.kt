package com.velagissellint.a65.useCase.contactDetails

import com.velagissellint.a65.DetailedInformationAboutContact
import io.reactivex.rxjava3.core.Single

interface ContactDetailsRepositoryCase {
    fun getContact(id: Int): Single<DetailedInformationAboutContact>?
}
