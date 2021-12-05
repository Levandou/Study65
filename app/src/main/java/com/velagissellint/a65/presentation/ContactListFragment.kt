package com.velagissellint.a65

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.velagissellint.a65.presentation.ContactListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactListFragment : Fragment(){
    private val contactListViewModel: ContactListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.title_for_ContactListFragment)
        val contactList = view.findViewById<ConstraintLayout>(R.id.contactList)
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        contactList.setOnClickListener {
            val contactDetailsFragment = ContactDetailsFragment.newInstance(1)
            transaction?.let {
                transaction
                    .addToBackStack(FRAG_CONTACTS_LIST)
                    .replace(R.id.fragment_container, contactDetailsFragment)
                    .commit()
            }
        }

        val permission =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            observeViewModel()
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 100)
        }
    }

    fun observeViewModel() {
        contactListViewModel.contactList.observe(viewLifecycleOwner, {
            val ivPhoto = requireView().findViewById<ImageView>(R.id.ivPhoto)
            activity?.runOnUiThread {
                ivPhoto.setImageURI(Uri.parse(it[0].imageResource))
                val tvName = requireView().findViewById<TextView>(R.id.tvName)
                val tvPhoneNumber = requireView().findViewById<TextView>(R.id.tvPhoneNumber)
                tvName.text = it[0].fullName
                tvPhoneNumber.text = it[0].phoneNumber
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_CONTACTS ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    observeViewModel()
                } else {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_CONTACTS),
                        READ_CONTACTS
                    )
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.no_access_to_contacts),
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }

    companion object {
        private const val FRAG_CONTACTS_LIST = "fragContactsList"
        const val READ_CONTACTS = 100
    }
}