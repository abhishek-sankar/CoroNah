package com.subzero.coviddiary.DataObjects

import android.location.Location

class FirebaseDataObject {
    val latitude : String
    val longitude : String
    val timestamp: String
    private var uploadedToDatabase : Boolean
    init{
         latitude = ""
         longitude = ""
         timestamp = ""
         uploadedToDatabase = false
    }
}