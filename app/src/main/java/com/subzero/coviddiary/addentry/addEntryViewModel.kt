package com.subzero.coviddiary.addentry

import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.Instant
import java.time.format.DateTimeFormatter

class addEntryViewModel : ViewModel() {
    var userInputText = ""
    var userGPS = ""
    var timeStamp = ""
    var modeOfTransport = ""
    lateinit var args : AddEntryFragmentArgs
    
    init{
        Log.i("Created Viewmodel","AddEntryFragment")
    }
    fun addCurrentDataToDatabase(){
        var database = Firebase.database.reference
//        database.child("/userList/${args.firebaseUId}/timestamps/${timeStamp}").setValue()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onTextChangedEditText(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        userInputText = p0.toString()
        timeStamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        modeOfTransport = args.modeOfTransport.toString()
//                viewModel.userGPS =
        Log.i("UserInput",userInputText)

    }
}