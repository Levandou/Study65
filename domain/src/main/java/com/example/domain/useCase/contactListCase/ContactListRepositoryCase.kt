package com.example.domain.useCase.contactListCase

import com.example.domain.DetailedInformationAboutContact
import io.reactivex.rxjava3.core.Single

 interface ContactListRepositoryCase {

    fun getContacts():Single<List<DetailedInformationAboutContact>>?
}