package gr.aytn.foodapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.window.OnBackInvokedDispatcher
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import gr.aytn.foodapp.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNavigationView,navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.loginFragment || destination.id == R.id.detailFragment ||
                destination.id == R.id.registerFragment || destination.id == R.id.splashFragment ) {
                binding.bottomNavigationView.visibility = View.GONE
            } else {
                binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }


    }


//    @Deprecated("Deprecated in Java")
//    override fun onBackPressed() {
//        val navController = navHostFragment.navController
//        if (navController.currentDestination?.id == R.id.profileFragment ||
//            navController.currentDestination?.id == R.id.cartFragment
//        ) {
//            navHostFragment.navController.navigate(R.id.homeFragment)
//        } else if (navController.currentDestination?.id == R.id.homeFragment) {
//            finish()
//        } else {
//            super.onBackPressed()
//        }
//
//    }


}