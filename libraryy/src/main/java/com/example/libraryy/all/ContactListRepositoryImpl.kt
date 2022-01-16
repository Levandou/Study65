package com.example.libraryy.all

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.domain.DetailedInformationAboutContact
import com.example.domain.useCase.broadcast.BroadcastRepositoryCase
import com.example.domain.useCase.contactDetails.ContactDetailsRepositoryCase
import com.example.domain.useCase.contactListCase.ContactListRepositoryCase
import com.example.libraryy.all.data.BroadcastReceiverForNotify
import com.example.libraryy.all.data.ContactsRepository
import com.velagissellint.a65.putNextBirthday
import io.reactivex.rxjava3.core.Single
import java.util.*

class ContactListRepositoryImpl(
    private val context: Context,
    private val contactsRepository: ContactsRepository
) :
    ContactListRepositoryCase, ContactDetailsRepositoryCase, BroadcastRepositoryCase {
    override fun getContacts(): Single<List<DetailedInformationAboutContact>>? {
        return contactsRepository.getContactsSingle()
    }

    override fun getContact(id: Int): Single<DetailedInformationAboutContact>? {
        return contactsRepository.getContact(id)
    }

    override fun offReminder(id: Int) {
        val intent = Intent(context, BroadcastReceiverForNotify::class.java)
        val alarmIntent = id?.let {
            PendingIntent.getBroadcast(
                context,
                it,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        alarmManager?.cancel(alarmIntent)
        alarmIntent?.cancel()
    }

    override fun onReminder(
        id: Int,
        detailedInformationAboutContact: DetailedInformationAboutContact
    ) {
        val intent = Intent(context, BroadcastReceiverForNotify::class.java)
        intent.putExtra(FULL_NAME, detailedInformationAboutContact.fullName)
        intent.putExtra(CONTACT_BIRTHDAY, detailedInformationAboutContact.birthday)
        intent.putExtra(CONTACT_ID, id)
        val alarmIntent = id?.let {
            PendingIntent.getBroadcast(
                context,
                it,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        alarmManager?.set(
            AlarmManager.RTC_WAKEUP,
            putNextBirthday(
                detailedInformationAboutContact.birthday,
                Calendar.getInstance()
            ).timeInMillis,
            alarmIntent
        )
    }

    companion object {
        private const val CONTACT_ID = "id"
        private const val FULL_NAME = "FULL_NAME"
        private const val CONTACT_BIRTHDAY = "contactBirthday"
    }
}