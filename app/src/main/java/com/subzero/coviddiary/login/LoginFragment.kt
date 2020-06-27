package com.subzero.coviddiary.login

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.subzero.coviddiary.databinding.FragmentLoginBinding
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.subzero.coviddiary.R

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    val RC_SIGN_IN = 1

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(
            inflater,
            R.layout.fragment_login, container, false
        )
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val user = FirebaseAuth.getInstance().currentUser
        Log.i("In onCreateView", "User deets is " + user)
//        if (user == null) {
            binding.firebaseLoginButton.setOnClickListener {
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                    RC_SIGN_IN
                )
            }
//        } else {
//            val navController = findNavController()
//            navController.navigate(R.id.checklistFragment)
//        }

//            Navigation.findNavController(this.requireView()).navigate(R.id.action_loginFragment_to_checklistFragment)
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            Log.i("It works", "Hurray")
            if (resultCode == Activity.RESULT_OK) {
//                val user = FirebaseAuth.getInstance().currentUser
//                if (response!!.isNewUser()) {
//                    if (user != null) {
//                        addUserToDatabase(user)
//                        Log.i("UserID : " + user.uid, "User Name : " + user.displayName)
//                    }
//                }
                Navigation.findNavController(this.requireView())
                    .navigate(R.id.action_loginFragment_to_checklistFragment)
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.

            }
        }
    }

    private fun addUserToDatabase(user: FirebaseUser) {
        val database = FirebaseDatabase.getInstance()
        Log.i("Inside addUser()", user.displayName)

<<<<<<< HEAD
        val myRef = database.getReference("userList")
        if (user != null) {
            myRef.child(user.uid).child("mailid").setValue(user.email)
            myRef.child(user.uid).child("name").setValue(user.displayName)
=======
        val myRef = database.getReference("/userList")
        if (user != null) {
            myRef.child(user.uid).setValue("Abhishek")
//            myRef.child(user.uid).child("userData").child("Name").setValue(user.displayName)
//            myRef.child(user.uid).child("UserData").child("Email").setValue(user.email)
>>>>>>> 7c9ed1a693b0932d227888dda0b899dffcb87565

        }

    }

}