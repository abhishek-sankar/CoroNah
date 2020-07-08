package com.subzero.coviddiary.Profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.subzero.coviddiary.R
import com.subzero.coviddiary.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    val activityTag = "Activity-ProfileFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val user = FirebaseAuth.getInstance().currentUser
        val binding = DataBindingUtil.inflate<FragmentProfileBinding>(inflater, R.layout.fragment_profile, container, false)
        Log.i(activityTag,user?.photoUrl.toString())
        binding.nameTextViewProfile.text = user?.displayName
        return binding.root
    }

}