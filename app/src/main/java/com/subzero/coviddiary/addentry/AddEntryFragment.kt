package com.subzero.coviddiary.addentry

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.subzero.coviddiary.R
import com.subzero.coviddiary.databinding.FragmentAddEntryBinding
import java.time.Instant
import java.time.format.DateTimeFormatter

class AddEntryFragment : Fragment() {
    private lateinit var viewModel : addEntryViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val args = AddEntryFragmentArgs.fromBundle(requireArguments())
        val binding = DataBindingUtil.inflate<FragmentAddEntryBinding>(inflater,
            R.layout.fragment_add_entry, container, false)
        viewModel = ViewModelProviders.of(this).get(addEntryViewModel::class.java)
        binding.editTextTextPersonName.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
//                TODO("Not yet implemented")
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                TODO("Not yet implemented")
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.userInputText = p0.toString()
                viewModel.timeStamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
                viewModel.modeOfTransport = args.modeOfTransport.toString()
//                viewModel.userGPS =
                Log.i("UserInput",viewModel.userInputText)
            }

        })

        return binding.root
    }
}