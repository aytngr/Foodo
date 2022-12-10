package gr.aytn.foodapp.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import gr.aytn.foodapp.R
import gr.aytn.foodapp.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        auth = Firebase.auth

        binding.btnSignUp.setOnClickListener {
            signUp(binding.gmail.text.toString(),binding.password.text.toString())
        }

        binding.goToSignIn.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.registerToLogin)
        }

        return binding.root
    }
    private fun validateForm(): Boolean {
        var valid = true

        val email = binding.gmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            binding.gmail.error = "Required."
            valid = false
        } else {
            binding.gmail.error = null
        }

        val password = binding.password.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.password.error = "Required."
            valid = false
        } else {
            binding.password.error = null
        }

        val passwordConfirm = binding.passwordConfirm.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.passwordConfirm.error = "Required."
            valid = false
        }else if(passwordConfirm != password){
            binding.passwordConfirm.error = "Passwords are not same."
        }
        else {
            binding.passwordConfirm.error = null
        }

        return valid
    }
    fun signUp(gmail: String, password: String){
        if (!validateForm()) {
            return
        }
        auth.createUserWithEmailAndPassword(gmail, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("auth", "createUserWithEmail:success")
                    val user = auth.currentUser
                    Navigation.findNavController(binding.btnSignUp)
                        .navigate(R.id.fromRegtoHome)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("auth", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
    }
}