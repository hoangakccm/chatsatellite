package com.example.demoappchat

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ProfileFragment : Fragment() {
    private lateinit var root : View
    private lateinit var username : String
    private lateinit var password : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_profile, container, false)
        initViews()
        initUIEventHandlers()
        return root
    }

    private fun initUIEventHandlers() {

    }

    private fun initViews() {
        val sharedPreferences = requireContext().getSharedPreferences("login_info", Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username", "").toString()
        password = sharedPreferences.getString("password", "").toString()
        var UsernamePf = root.findViewById<TextView>(R.id.userNamePf)
        UsernamePf.text = username
    }

}