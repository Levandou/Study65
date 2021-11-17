package com.velagissellint.a65

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class ContactDetailsFragment : Fragment(), DataFromService, CompoundButton.OnCheckedChangeListener {
    private lateinit var contactDetails: DetailedInformationAboutContact
    private var id: Int? = null
    private var service: ContactService.Service? = null
    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100


    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private var switchAlarm: Switch? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ContactService.Service) {
            service = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt(ID_ARG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.title_for_ContactDetailsFragment)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return inflater.inflate(R.layout.fragment_contact_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        switchAlarm = requireView().findViewById(R.id.SwitchBirthday)
        switchAlarm?.setOnCheckedChangeListener(this)
        val permission =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            id?.let { service?.getService()?.getContact(contactDetailCallback, it) }
        } else {
            getPermission()
        }
    }

    private val contactDetailCallback = object : GetContactDetail {
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        override fun onSuccess(contact: DetailedInformationAboutContact) {
            contactDetails = contact
            val ivPhoto = requireView().findViewById<ImageView>(R.id.imvPhoto)
            val tvName = requireView().findViewById<TextView>(R.id.tvName)
            val tvPhoneNumber = requireView().findViewById<TextView>(R.id.phoneNumber)
            val email = requireView().findViewById<TextView>(R.id.email)
            val description = requireView().findViewById<TextView>(R.id.description)
            val switchNotify = requireView().findViewById<Switch>(R.id.SwitchBirthday)

            activity?.runOnUiThread {
                ivPhoto.setImageURI(Uri.parse(contact.imageResource))
                tvName.text = contact.fullName
                tvPhoneNumber.text = contact.phoneNumber
                email.text = contact.email
                description.text = contact.description
                if (isAlarmSet(requireContext())) {
                    switchNotify.isChecked = true
                } else {
                    switchNotify.isChecked = false
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                service?.getService()?.getContact(contactDetailCallback, 0)
            }
        } else {
            getPermission()
            Toast.makeText(
                requireContext(),
                getString(R.string.no_access_to_contacts),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun isAlarmSet(context: Context): Boolean {
        val intent = Intent(context, BroadcastReceiverForNotify::class.java)
        val alarmIntent = id?.let {
            PendingIntent.getBroadcast(
                context,
                it,
                intent,
                PendingIntent.FLAG_NO_CREATE
            )
        }
        return alarmIntent != null
    }

    override fun setData() {
        service?.getService()?.getContact(contactDetailCallback, 0)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            val intent = Intent(context, BroadcastReceiverForNotify::class.java)
            intent.putExtra(FULL_NAME, contactDetails.fullName)
            intent.putExtra(CONTACT_BIRTHDAY, contactDetails.birthday)
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
                putNextBirthday(contactDetails.birthday),
                alarmIntent
            )
        } else {
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
    }

    override fun onDetach() {
        service = null
        super.onDetach()
    }

    override fun onDestroyView() {
        switchAlarm = null
        super.onDestroyView()
    }

    fun getPermission(){
        requestPermissions(
            arrayOf(Manifest.permission.READ_CONTACTS),
            PERMISSIONS_REQUEST_READ_CONTACTS)
    }

    companion object {
        private const val CONTACT_ID = "id"
        private const val FULL_NAME = "FULL_NAME"
        private const val CONTACT_BIRTHDAY = "contactBirthday"
        private const val ID_ARG = "id"
        fun newInstance(id: Int) = ContactDetailsFragment().apply {
            arguments = bundleOf(ID_ARG to id)
        }
    }
}

interface GetContactDetail {
    fun onSuccess(contact: DetailedInformationAboutContact)
}