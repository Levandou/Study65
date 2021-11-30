package com.velagissellint.a65.presentation.contactList

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.velagissellint.a65.R
import com.velagissellint.a65.domain.DetailedInformationAboutContact
import com.velagissellint.a65.presentation.contactDetails.ContactDetailsFragment

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

class DividerItemDecoration(context: Context?) : ItemDecoration() {
    private var divider: Drawable? = null

    init {
        divider = context?.let { ContextCompat.getDrawable(it, R.drawable.divider) }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + divider!!.intrinsicHeight
            divider?.setBounds(left, top, right, bottom)
            divider?.draw(c)
        }
    }
}