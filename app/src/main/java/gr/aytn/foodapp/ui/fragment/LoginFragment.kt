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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import gr.aytn.foodapp.R
import gr.aytn.foodapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container, false)

        auth = Firebase.auth

        binding.btnSignIn.setOnClickListener {
            signIn(binding.gmail.text.toString(),binding.password.text.toString())
        }

        binding.goToRegister.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.loginToRegister)
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

        return valid
    }
    fun signIn(gmail: String, password: String){
        if (!validateForm()) {
            return
        }
        auth.signInWithEmailAndPassword(gmail, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("auth", "signInWithEmail:success")
                    val user = auth.currentUser
                    Navigation.findNavController(binding.btnSignIn)
                        .navigate(R.id.fromLoginToHome)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("auth", "signInWithEmail:failure", task.exception)
                    Toast.makeText(requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

}