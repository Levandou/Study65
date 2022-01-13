package com.velagissellint.a65.useCase.contactDetails

import com.velagissellint.a65.DetailedInformationAboutContact
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetContactDetailsUseCase @Inject constructor(private val contactDetailsRepositoryCase: ContactDetailsRepositoryCase) {
    private var id: Int = 0

    fun idOfContact(idOfContact: Int) {
        id = idOfContact
    }

    fun getContact(): Single<DetailedInformationAboutContact>? =
        contactDetailsRepositoryCase.getContact(id)
}