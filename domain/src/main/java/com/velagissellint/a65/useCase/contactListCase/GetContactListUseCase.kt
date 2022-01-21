package com.velagissellint.a65.useCase.contactListCase

import com.velagissellint.a65.DetailedInformationAboutContact
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetContactListUseCase @Inject constructor(private val contactListRepositoryCase: ContactListRepositoryCase) {
    fun getContacts(): Single<List<DetailedInformationAboutContact>>? {
        return contactListRepositoryCase.getContacts()
    }
}
