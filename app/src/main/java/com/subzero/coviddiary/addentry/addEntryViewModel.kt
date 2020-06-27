package com.subzero.coviddiary.addentry

import android.util.Log
import androidx.lifecycle.ViewModel

class addEntryViewModel : ViewModel() {
    var userInputText = ""
    var userGPS = ""
    var timeStamp = ""
    var modeOfTransport = ""
    init{
        Log.i("Created Viewmodel","AddEntryFragment")
    }
}