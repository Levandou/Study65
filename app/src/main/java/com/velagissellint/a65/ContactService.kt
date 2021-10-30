package com.velagissellint.a65

import android.app.Service
import android.content.Intent
import android.os.Binder
import kotlin.concurrent.thread

class ContactService : Service() {
    private val binder = ContactListBinder()
    val detailedInformationAboutContact = DetailedInformationAboutContact()
    val contactsList = listOf(detailedInformationAboutContact)

    override fun onBind(intent: Intent) = binder

    inner class ContactListBinder : Binder() {
        fun getService(): ContactService = this@ContactService
    }

    fun getContacts(callback: GetContactList) {
        thread(start = true) {
            callback.onSuccess(contactsList)
        }
    }

    fun getContact(callback: GetContactDetail, id: Int) {
        thread(start = true) {
            callback.onSuccess(contactsList[id])
        }
    }

    interface Service {
        fun getService(): ContactService?
    }
}