package com.example.domain.useCase.contactDetails

import com.example.domain.DetailedInformationAboutContact
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetContactDetailsUseCase @Inject constructor(private val contactDetailsRepositoryCase: ContactDetailsRepositoryCase) {
    private var id:Int=0

    fun idOfContact(idOfContact:Int){
        id=idOfContact
    }

    fun getContact(): Single<DetailedInformationAboutContact>? {
        return contactDetailsRepositoryCase.getContact(id)

    }
}