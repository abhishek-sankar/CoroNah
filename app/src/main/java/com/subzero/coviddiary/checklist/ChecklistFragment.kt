package com.subzero.coviddiary.checklist

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.subzero.coviddiary.R
import com.subzero.coviddiary.databinding.FragmentChecklistBinding
class ChecklistFragment : Fragment() {

    private lateinit var  viewModel : CheckListViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(CheckListViewModel::class.java)
        Log.i("CurrentUser : ",viewModel.user!!.displayName)
        val binding = DataBindingUtil.inflate<FragmentChecklistBinding>(inflater,
            R.layout.fragment_checklist, container, false)
        val arrayAdapter = ArrayAdapter<String>(requireContext(),
            R.layout.spinner_layout_min,viewModel.listOfModesOfTransport)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
//        viewModel.database.child("/userList/${viewModel.user!!.uid}/timestamps/timestamp/from").setValue("Trivandrum")
        with(binding.spinnerModeOfTransport){
            adapter = arrayAdapter
            setSelection(0,false)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    viewModel.onNothingSelectedSpinner()
                }
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    viewModel.onItemSelectedSpinner(p1!!,p2)
                    binding.spinnerModeOfTransport.setSelection(0,false)
                }
            }
        }
        binding.modeOfTransportTextView.text = getString(R.string.add_entry_prompt,(viewModel.user!!.displayName)?.split("\\s".toRegex())?.first())
        return binding.root
    }
    private fun showToast(context: Context = requireContext(), message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }

}