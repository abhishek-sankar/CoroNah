package com.subzero.coviddiary.Settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.subzero.coviddiary.DataObjects.LocationViewModel
import com.subzero.coviddiary.R


class SettingsFragment : Fragment() {
    private lateinit var viewModel : LocationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)



    }
}