package com.subzero.coviddiary.checklist

import android.util.Log
import androidx.lifecycle.ViewModel
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
}