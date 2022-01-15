package com.velagissellint.a65.presentation.contactDetails

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
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.velagissellint.a65.R
import com.velagissellint.a65.applicationComponent
import com.velagissellint.a65.data.BroadcastReceiverForNotify
import com.velagissellint.a65.presentation.ViewModelFactory
import com.velagissellint.a65.putNextBirthday
import javax.inject.Inject

class ContactDetailsFragment : Fragment(), CompoundButton.OnCheckedChangeListener {
    @Inject
    lateinit var factory: ViewModelFactory
    lateinit var contactDetailsViewModel: ContactDetailsViewModel

    private var id: Int? = null
    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private var switchAlarm: Switch? = null

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

        applicationComponent.plusContactDetailsComponent().inject(this)
        contactDetailsViewModel =
            ViewModelProviders.of(this, factory).get(ContactDetailsViewModel::class.java)

        return inflater.inflate(R.layout.fragment_contact_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactDetailsViewModel.getContact(id)

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        switchAlarm = requireView().findViewById(R.id.SwitchBirthday)
        switchAlarm?.setOnCheckedChangeListener(this)
        observeProgressBar(progressBar)
        val permission =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            observeViewModel()
        } else {
            getPermission()
        }
    }

    private fun observeProgressBar(progressBar: ProgressBar) {
        contactDetailsViewModel
            .isLoadingPublic
            .observe(viewLifecycleOwner, {
                progressBar.isVisible = it
            })
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    fun observeViewModel() {
        contactDetailsViewModel.contact.observe(viewLifecycleOwner, {
            val ivPhoto = requireView().findViewById<ImageView>(R.id.imvPhoto)
            val tvName = requireView().findViewById<TextView>(R.id.tvName)
            val tvPhoneNumber = requireView().findViewById<TextView>(R.id.phoneNumber)
            val email = requireView().findViewById<TextView>(R.id.email)
            val description = requireView().findViewById<TextView>(R.id.description)
            val switchNotify = requireView().findViewById<Switch>(R.id.SwitchBirthday)
            it?.let { contact ->
                activity?.runOnUiThread {
                    ivPhoto.setImageURI(Uri.parse(contact.imageResource))
                    tvName.text = contact.fullName
                    tvPhoneNumber.text = contact.phoneNumber
                    email.text = contact.email
                    description.text = contact.description
                    switchNotify.isChecked = isAlarmSet(requireContext())
                }
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                observeViewModel()
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

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun isAlarmSet(context: Context): Boolean {
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

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            val intent = Intent(context, BroadcastReceiverForNotify::class.java)
            intent.putExtra(FULL_NAME, contactDetailsViewModel.contact.value?.fullName)
            intent.putExtra(CONTACT_BIRTHDAY, contactDetailsViewModel.contact.value?.birthday)
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
                putNextBirthday(contactDetailsViewModel.contact.value?.birthday),
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

    override fun onDestroyView() {
        switchAlarm = null
        super.onDestroyView()
    }

    private fun getPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_CONTACTS),
            PERMISSIONS_REQUEST_READ_CONTACTS
        )
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