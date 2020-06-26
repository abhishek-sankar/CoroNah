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
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.subzero.coviddiary.R

class LoginFragment : Fragment() {
    val RC_SIGN_IN = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(inflater,
            R.layout.fragment_login,container,false)
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build())
        val user = FirebaseAuth.getInstance().currentUser
        Log.i("In onCreateView","User deets is "+user)
        if (user==null) {
            binding.firebaseLoginButton.setOnClickListener {
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                    RC_SIGN_IN
                )
            }
        }else{
            val navController = findNavController()
            navController.navigate(R.id.checklistFragment)
        }

//            Navigation.findNavController(this.requireView()).navigate(R.id.action_loginFragment_to_checklistFragment)
        return binding.root
    }

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
//            Navigation.findNavController().navigate(R.id.action_loginFragment_to_checklistFragment)
            Log.i("It works","Hurray")
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                Navigation.findNavController(this.requireView()).navigate(R.id.action_loginFragment_to_checklistFragment)
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
}