package com.velagissellint.a65

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.util.Log
import kotlin.concurrent.thread

class ContactService : Service() {
    private val binder = ContactListBinder()
    val contactSource=ContactSource()

    override fun onBind(intent: Intent) = binder

    inner class ContactListBinder : Binder() {
        fun getService(): ContactService = this@ContactService
    }

    fun getContacts(callback: GetContactList) {
        thread(start = true) {
            callback.onSuccess(contactSource.getContacts(contentResolver))
        }
    }

    fun getContact(callback: GetContactDetail, id: Int) {
        thread(start = true) {
            callback.onSuccess(contactSource.getContact(contentResolver,id))
        }
    }

    interface Service {
        fun getService(): ContactService?
    }
}