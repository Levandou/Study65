package com.velagissellint.a65.presentation.contactList

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.velagissellint.a65.R

class DetailedInformationViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val ivPhoto = view.findViewById<ImageView>(R.id.ivPhoto)
    val tvName = view.findViewById<TextView>(R.id.tvName)
    val tvPhoneNumber = view.findViewById<TextView>(R.id.tvPhoneNumber)
}