package com.subzero.coviddiary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.subzero.coviddiary.databinding.FragmentAddEntryBinding

class AddEntryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentAddEntryBinding>(inflater, R.layout.fragment_add_entry, container, false)
        return binding.root
    }
}