package gr.aytn.foodapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import gr.aytn.foodapp.R
import gr.aytn.foodapp.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        val auth = Firebase.auth
        lifecycleScope.launch{
            delay(1000)
            binding.animationSplash.playAnimation()
            delay(2000)
            val currentUser = auth.currentUser
            if(currentUser != null){
                Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
                    .navigate(R.id.splashToHome)
            }else{
                Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
                    .navigate(R.id.splashToLogin)
            }
        }

        return binding.root
    }


}