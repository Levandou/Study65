package com.velagissellint.a65.data

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.provider.ContactsContract
import com.velagissellint.a65.domain.ContactFields
import com.velagissellint.a65.domain.DetailedInformationAboutContact
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

class ContactsRepository @Inject constructor (
    private val contentResolver: ContentResolver
) {
    fun getContacts(): List<DetailedInformationAboutContact> {
        val contactList: MutableList<DetailedInformationAboutContact> = mutableListOf()
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null, null
        )

        cursor?.use {
            while (it.moveToNext()) {
                contactList.add(
                    DetailedInformationAboutContact(
                        getField(it.getString(it.getColumnIndex(ContactsContract.Contacts._ID)), ContactFields.IMAGE,contentResolver),
                        fullName = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)),
                        phoneNumber = getField(it.getString(it.getColumnIndex(ContactsContract.Contacts._ID)), ContactFields.PHONE,contentResolver),
                        id = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
                            .toInt(),
                    )
                )
            }
        }
        return contactList
    }

     fun getField(id: String, contactFields: ContactFields, contentResolver:ContentResolver): String {
        var meaning: String? =""
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

    @SuppressLint("SimpleDateFormat")
    private fun formatterOfBirthday(id:Int): Calendar {
        var birthdayString: String = getField(id = id.toString(), ContactFields.BIRTHDAY,contentResolver)
        birthdayString = birthdayString.dropWhile { it == ']' }
        birthdayString = birthdayString.drop(1)
        val birthday = Calendar.getInstance()
        val dateFormatBirthday = SimpleDateFormat(PATTERN_DATA)
        birthday.time = dateFormatBirthday.parse(birthdayString)
        return birthday
    }

    private fun getName(contentResolver:ContentResolver): String {
        var name = ""
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))
                } while (it.moveToNext())
            }
        }
        return name
    }


     fun getContact(id: Int): DetailedInformationAboutContact {
        val detailedInformationAboutContact = DetailedInformationAboutContact(
            getField(id.toString(), ContactFields.IMAGE,contentResolver),
            getName(contentResolver),
            getField(id.toString(), ContactFields.PHONE,contentResolver),
            getField(id.toString(), ContactFields.EMAIL,contentResolver),
            description = getField(id.toString(), ContactFields.DESCRIPTION,contentResolver),
            formatterOfBirthday(id)
        )
        return detailedInformationAboutContact
    }
    companion object{
        val PATTERN_DATA="yyyy-MM-dd"
    }
}