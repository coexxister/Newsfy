package com.example.newsfy_rework.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.newsfy_rework.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth

class AccountFragment: Fragment() {

    private lateinit var auth: FirebaseAuth
    private var preferences: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.account_activity, container, false)
        auth = FirebaseAuth.getInstance()
        val logout : Button = root.findViewById(R.id.logout_button)
        var topicsBox: ChipGroup = root.findViewById(R.id.topics_frame)
        var textViewEmail: TextView = root.findViewById(R.id.email_address)
        preferences = this.arguments?.getStringArrayList("preferences") as ArrayList<String>


        for(item in preferences){
            val chip = Chip(topicsBox.context)
            chip.text = item
            topicsBox.addView(chip)
        }

        textViewEmail.text = auth.currentUser?.email

        logout.setOnClickListener {it
            signOut()
        }

        return root
    }

    private fun signOut() {
        auth.signOut()
    }
}