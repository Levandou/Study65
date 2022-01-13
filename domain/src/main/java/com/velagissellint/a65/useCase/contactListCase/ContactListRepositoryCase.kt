package com.velagissellint.a65.useCase.contactListCase

import com.velagissellint.a65.DetailedInformationAboutContact
import io.reactivex.rxjava3.core.Single

interface ContactListRepositoryCase {
    fun getContacts(): Single<List<DetailedInformationAboutContact>>?
}