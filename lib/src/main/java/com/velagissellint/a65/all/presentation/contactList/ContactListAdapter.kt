package com.velagissellint.a65.all.presentation.contactList

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.ListAdapter
import com.velagissellint.a65.DetailedInformationAboutContact
import com.velagissellint.a65.all.presentation.contactDetails.ContactDetailsFragment
import com.velagissellint.a655.R

class ContactListAdapter(private val transaction: FragmentTransaction?) :
    ListAdapter<DetailedInformationAboutContact, DetailedInformationViewHolder>(
        DetailedInformationDiffCallback()
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailedInformationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_contact,
            parent,
            false
        )
        return DetailedInformationViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailedInformationViewHolder, position: Int) {
        holder.ivPhoto.setImageURI(Uri.parse(getItem(position).imageResource))
        holder.tvName.text = getItem(position).fullName
        holder.tvPhoneNumber.text = getItem(position).phoneNumber
        holder.view.setOnClickListener {
            val contactDetailsFragment = ContactDetailsFragment.newInstance(position)
            transaction?.let {
                transaction
                    .addToBackStack(FRAG_CONTACTS_LIST)
                    .replace(R.id.fragment_container, contactDetailsFragment)
                    .commit()
            }
        }
    }

    companion object {
        private const val FRAG_CONTACTS_LIST = "fragContactsList"
    }
}
