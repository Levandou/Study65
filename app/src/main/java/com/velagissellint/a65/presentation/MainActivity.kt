package com.velagissellint.a65

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFragmentManager.popBackStack(
                    IDFORBACKSTACK,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val IDFORBACKSTACK = "fragContactsList"
    }
}