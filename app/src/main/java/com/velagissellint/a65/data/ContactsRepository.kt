package com.velagissellint.a65.data

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.provider.ContactsContract
import com.velagissellint.a65.domain.ContactFields
import com.velagissellint.a65.domain.DetailedInformationAboutContact
import io.reactivex.rxjava3.core.Single
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

class ContactsRepository @Inject constructor(
    private val contentResolver: ContentResolver
) {
    @SuppressLint("Range")
    fun getContacts(): List<DetailedInformationAboutContact> {
        val contactList: MutableList<DetailedInformationAboutContact> = mutableListOf()
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null, null
        )

        cursor?.use {
            while (it.moveToNext()) {
                contactList.add(
                    DetailedInformationAboutContact(
                        getField(
                            it.getString(it.getColumnIndex(ContactsContract.Contacts._ID)),
                            ContactFields.IMAGE,
                            contentResolver
                        ),
                        fullName = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)),
                        phoneNumber = getField(
                            it.getString(it.getColumnIndex(ContactsContract.Contacts._ID)),
                            ContactFields.PHONE,
                            contentResolver
                        ),
                        id = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
                            .toInt(),
                    )
                )
            }
        }
        return contactList
    }

    @SuppressLint("Range")
    fun getField(
        id: String,
        contactFields: ContactFields,
        contentResolver: ContentResolver
    ): String {
        var meaning: String? = ""
        val uri: Uri
        val selection: String
        val columnData: String
        val selectionArgs: Array<String>

        when (contactFields) {

            ContactFields.IMAGE -> {
                uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"
                columnData = ContactsContract.CommonDataKinds.Phone.PHOTO_URI
                selectionArgs = arrayOf(id)

            }

            ContactFields.PHONE -> {
                uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"
                columnData = ContactsContract.CommonDataKinds.Phone.DATA
                selectionArgs = arrayOf(id)
            }

            ContactFields.EMAIL -> {
                uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI
                selection = ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?"
                columnData = ContactsContract.CommonDataKinds.Email.DATA
                selectionArgs = arrayOf(id)
            }

            ContactFields.DESCRIPTION -> {
                uri = ContactsContract.Data.CONTENT_URI
                selection =
                    ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?"
                columnData = ContactsContract.CommonDataKinds.Note.NOTE
                selectionArgs = arrayOf(id, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
            }

            ContactFields.BIRTHDAY -> {
                uri = ContactsContract.Data.CONTENT_URI
                selection = ContactsContract.Data.CONTACT_ID + " = ? AND " +
                        ContactsContract.Data.MIMETYPE + " = ? AND " +
                        ContactsContract.CommonDataKinds.Event.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY
                columnData = ContactsContract.CommonDataKinds.Event.START_DATE
                selectionArgs =
                    arrayOf(id, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
            }
        }

        val cursor = contentResolver.query(uri, null, selection, selectionArgs, null)
        cursor?.use {
            while (it.moveToNext()) {
                meaning = it.getString(it.getColumnIndex(columnData))
            }
        }
        return meaning.toString()
    }

    fun formatterOfBirthday(id: Int): Calendar {
        var birthdayString: String =
            getField(id = id.toString(), ContactFields.BIRTHDAY, contentResolver)
        birthdayString = removeScoops(birthdayString)
        val birthday = Calendar.getInstance()
        val dateFormatBirthday = SimpleDateFormat(ContactsRepository.PATTERN_DATA)
        birthday.time = dateFormatBirthday.parse(birthdayString)
        return birthday
    }

    private fun removeScoops(birthdayString: String): String {
        birthdayString.dropWhile { it == ']' }
        birthdayString.drop(1)
        return birthdayString
    }

    fun getContactsSingle()=Single.fromCallable{getContacts()}

    fun getContact(id: Int):Single<DetailedInformationAboutContact> {
        val idToGet = id + 1
        return Single.fromCallable {
            DetailedInformationAboutContact(
                getField(idToGet.toString(), ContactFields.IMAGE, contentResolver),
                getContacts()[id].fullName,
                getField(idToGet.toString(), ContactFields.PHONE, contentResolver),
                getField(idToGet.toString(), ContactFields.EMAIL, contentResolver),
                description = getField(
                    idToGet.toString(),
                    ContactFields.DESCRIPTION,
                    contentResolver
                ),
                formatterOfBirthday(idToGet)
            )
        }
    }

    companion object {
        val PATTERN_DATA = "yyyy-MM-dd"
    }
}