package com.subzero.coviddiary.checklist

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.subzero.coviddiary.R
import com.subzero.coviddiary.databinding.FragmentChecklistBinding
class ChecklistFragment : Fragment() {
    var modeOfTransport = ""
    val listOfModesOfTransport = arrayOf("Private Transport","Public Transport","Walk")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentChecklistBinding>(inflater,
            R.layout.fragment_checklist, container, false)
        val arrayAdapter = ArrayAdapter<String>(requireContext(),
            R.layout.spinner_layout_min,listOfModesOfTransport)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        binding.spinnerModeOfTransport.adapter = arrayAdapter
        binding.spinnerModeOfTransport.setSelection(0,false)
        Log.i("Yes, Im getting this","Above override methods")
        binding.spinnerModeOfTransport.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Did you go to a hotel?")
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.i("checkListFrag",listOfModesOfTransport[p2])
                modeOfTransport = listOfModesOfTransport[p2]
                Navigation.findNavController(p1!!).navigate(R.id.action_checklistFragment_to_addEntryFragment)
                binding.spinnerModeOfTransport.setSelection(0,false)
            }
        }
        return binding.root
    }
    private fun showToast(context: Context = requireContext(), message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }
}