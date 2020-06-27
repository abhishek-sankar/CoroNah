package com.subzero.coviddiary.checklist

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CheckListViewModel : ViewModel() {
    var modeOfTransport = ""
    val listOfModesOfTransport = arrayOf("Private Transport","Public Transport","Walk")
    val myDatabase = Firebase.database
    val database = myDatabase.reference
    val user = FirebaseAuth.getInstance().currentUser


    init{
        Log.i("In OnCreate","Created CheckListViewModel")
    }
    fun onItemSelectedSpinner(p1: View, p2: Int){
        Log.i("checkListFrag",listOfModesOfTransport[p2])
        modeOfTransport = listOfModesOfTransport[p2].toString()
        Navigation.findNavController(p1).navigate(ChecklistFragmentDirections.actionChecklistFragmentToAddEntryFragment(modeOfTransport))
    }

    fun onNothingSelectedSpinner() {
        TODO("Not yet implemented")
    }
}