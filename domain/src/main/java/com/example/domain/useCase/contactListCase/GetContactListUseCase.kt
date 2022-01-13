package com.example.domain.useCase.contactListCase

import com.example.domain.DetailedInformationAboutContact
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetContactListUseCase @Inject constructor(private val contactListRepositoryCase: ContactListRepositoryCase) {

    fun getContacts(): Single<List<DetailedInformationAboutContact>>? {
        return contactListRepositoryCase.getContacts()
    }
}