package com.velagissellint.a65

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class ContactListFragment : Fragment() ,DataFromService{
    private var service: ContactService.Service? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ContactService.Service) {
            service = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.title_for_ContactListFragment)
        val contactList = view.findViewById<ConstraintLayout>(R.id.contactList)
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        contactList.setOnClickListener {
            val contactDetailsFragment = ContactDetailsFragment.newInstance("contact1")
            transaction?.let {
                transaction
                        .addToBackStack(FRAG_CONTACTS_LIST)
                        .replace(R.id.fragment_container, contactDetailsFragment)
                        .commit()
            }
        }
        service?.getService()?.getContacts(contactListCallback)
    }

    private val contactListCallback = object : GetContactList {
        override fun onSuccess(list: List<DetailedInformationAboutContact>) {

                    val ivPhoto = requireView().findViewById<ImageView>(R.id.ivPhoto)
                    val tvName = requireView().findViewById<TextView>(R.id.tvName)
                    val tvPhoneNumber = requireView().findViewById<TextView>(R.id.tvPhoneNumber)
                    activity?.runOnUiThread {
                        ivPhoto.setImageDrawable(ContextCompat.getDrawable(requireContext(), list[0].imageResource))
                        tvName.text = list[0].fullName
                        tvPhoneNumber.text = list[0].phoneNumber
            }
        }
    }

    override fun onDetach() {
        service = null
        super.onDetach()
    }

    companion object {
        private const val FRAG_CONTACTS_LIST = "fragContactsList"
    }

    override fun setData() {
        service?.getService()?.getContacts((contactListCallback))
    }
}

interface GetContactList {
    fun onSuccess(list: List<DetailedInformationAboutContact>)
}