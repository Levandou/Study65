package com.velagissellint.a65

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class ContactDetailsFragment : Fragment(),DataFromService {
    private var id: String? = null
    private var service: ContactService.Service? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ContactService.Service) {
            service = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ID_ARG)
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
        service?.getService()?.getContact(contactDetailCallback, 0)
    }

    private val contactDetailCallback = object : GetContactDetail {
        override fun onSuccess(contact: DetailedInformationAboutContact) {
            val ivPhoto = requireView().findViewById<ImageView>(R.id.imvPhoto)
            val tvName = requireView().findViewById<TextView>(R.id.tvName)
            val tvPhoneNumber = requireView().findViewById<TextView>(R.id.phoneNumber)
            val email = requireView().findViewById<TextView>(R.id.email)
            val description = requireView().findViewById<TextView>(R.id.description)

            activity?.runOnUiThread {
                ivPhoto.setImageDrawable(ContextCompat.getDrawable(requireContext(), contact.imageResource))
                tvName.text = contact.fullName
                tvPhoneNumber.text = contact.phoneNumber
                email.text = contact.email
                description.text = contact.description
            }
        }
    }

    override fun onDetach() {
        service = null
        super.onDetach()
    }

    companion object {
        private const val ID_ARG = "id"
        fun newInstance(id: String) = ContactDetailsFragment().apply {
            arguments = bundleOf(ID_ARG to id)
        }
    }

    override fun setData() {
        service?.getService()?.getContact(contactDetailCallback,0)
    }
}

interface GetContactDetail {
    fun onSuccess(contact: DetailedInformationAboutContact)
}