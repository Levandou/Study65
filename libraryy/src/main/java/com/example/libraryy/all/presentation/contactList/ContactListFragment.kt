package com.example.libraryy.all.presentation.contactList

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.libraryy.R
import com.example.libraryy.all.containersDi.ContainerAppContainer
import com.example.libraryy.all.presentation.ViewModelFactory
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import javax.inject.Inject

class ContactListFragment : Fragment() {
    @Inject
    lateinit var factory: ViewModelFactory
    lateinit var contactListViewModel: ContactListViewModel

    private lateinit var adapter: ContactListAdapter
    private lateinit var progressBar: ProgressBar
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as ContainerAppContainer).appContainer()?.plusPersonListComponent()
            ?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        contactListViewModel =
            ViewModelProviders.of(this, factory).get(ContactListViewModel::class.java)
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = view.findViewById(R.id.progressBar)
        setHasOptionsMenu(true)
        activity?.title = getString(R.string.title_for_ContactListFragment)
        setupRecyclerView(view)
        observeProgressBar(progressBar)
        observeSearchView()
        val permission =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            observeViewModel()
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 100)
        }
    }

    private fun observeProgressBar(progressBar: ProgressBar) {
        contactListViewModel
            .isLoadingPublic
            .observe(viewLifecycleOwner, {
                progressBar.isVisible = it
            })
    }

    private fun observeViewModel() {
        contactListViewModel.contactList.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    private fun observeSearchView() {
        contactListViewModel.contactListFilter.observe(viewLifecycleOwner, {
            if (it.isEmpty())
                Toast.makeText(
                    requireContext(),
                    requireContext().getString(R.string.contact_not_found),
                    Toast.LENGTH_LONG
                ).show()
            adapter.submitList(it)
        })
    }

    @SuppressLint("UseRequireInsteadOfGet")
    fun setupRecyclerView(view: View) {
        val rvContactList = view.findViewById<RecyclerView>(R.id.rv_pokemon_list)
        rvContactList.addItemDecoration(DividerItemDecoration(activity?.applicationContext))
        adapter = ContactListAdapter(getFragmentTransaction())
        rvContactList.adapter = adapter
    }

    private fun getFragmentTransaction(): FragmentTransaction? {
        return activity?.supportFragmentManager?.beginTransaction()

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
        var observable: Observable<String?>
        val searchItem: MenuItem = menu.findItem(R.id.actionSearch)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                observable = Observable.create { text: ObservableEmitter<String?> ->
                    text.onNext(
                        newText
                    )
                }
                contactListViewModel.filter(observable)
                return false
            }
        })
    }

    companion object {
        const val READ_CONTACTS = 100
    }
}