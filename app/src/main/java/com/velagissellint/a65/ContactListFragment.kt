package com.velagissellint.a65

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment

class ContactListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title =getString(R.string.title_for_ContactListFragment)
        val contactList=view.findViewById<ConstraintLayout>(R.id.contactList)
        val transaction=activity?.supportFragmentManager?.beginTransaction()
        contactList.setOnClickListener {
             val contactDetailsFragment= ContactDetailsFragment.newInstance("contact1")
                transaction?.let {
                    transaction
                            .addToBackStack(fragContactsList)
                            .replace(R.id.fragment_container, contactDetailsFragment)
                            .commit()
                }
        }
    }

    companion object{
        private const val fragContactsList="fragContactsList"
    }
}