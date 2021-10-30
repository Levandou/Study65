package com.velagissellint.a65

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity(), ContactService.Service {
    private var contactService: ContactService? = null
    private var bound = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction
                    .addToBackStack(null)
                    .add(R.id.fragment_container, ContactListFragment())
                    .commit()
        }
        val intent = Intent(this, ContactService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as ContactService.ContactListBinder
            contactService = binder.getService()
            val dataManager = supportFragmentManager.fragments.lastOrNull() as? DataFromService
            dataManager?.setData()
            bound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFragmentManager.popBackStack(IDFORBACKSTACK, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getService(): ContactService? {
        return contactService
    }

    override fun onDestroy() {
        if (bound) {
            unbindService(connection)
            bound = false
        }
        super.onDestroy()
    }

    companion object {
        private const val IDFORBACKSTACK = "fragContactsList"
    }
}

interface DataFromService {
    fun setData()
}