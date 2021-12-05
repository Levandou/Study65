package com.velagissellint.a65.presentation.contactList

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.velagissellint.a65.R
import com.velagissellint.a65.domain.DetailedInformationAboutContact
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactListFragment : Fragment() {
    private val contactListViewModel: ContactListViewModel by viewModels()
    private lateinit var adapter: ContactListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.title_for_ContactListFragment)
        setupRecyclerView(view)
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
            adapter.submitList(it)
        })
    }

    @SuppressLint("UseRequireInsteadOfGet")
    fun setupRecyclerView(view: View) {
        val rvContactList = view.findViewById<RecyclerView>(R.id.rv_pokemon_list)
        rvContactList.addItemDecoration(DividerItemDecoration(activity!!.applicationContext))
        adapter = ContactListAdapter(activity?.supportFragmentManager?.beginTransaction())
        rvContactList.adapter = adapter
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.actionSearch)
        val searchView: SearchView = searchItem.getActionView() as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filter(it) }
                return false
            }
        })
    }

    private fun filter(text: String) {
        val filteredlist: MutableList<DetailedInformationAboutContact> = mutableListOf()

        for (item in contactListViewModel.contactList.value!!) {
            if (item.fullName.toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(requireContext(), "Данные не найдены", Toast.LENGTH_SHORT).show()
        } else {
            adapter.submitList(filteredlist)
        }
    }

    companion object {
        const val READ_CONTACTS = 100
    }
}

