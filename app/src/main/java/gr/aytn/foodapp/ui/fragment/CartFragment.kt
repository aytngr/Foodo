package gr.aytn.foodapp.ui.fragment

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import gr.aytn.foodapp.MainActivity
import gr.aytn.foodapp.R
import gr.aytn.foodapp.databinding.FragmentCartBinding
import gr.aytn.foodapp.ui.adapter.FoodCartAdapter
import gr.aytn.foodapp.ui.viewmodel.CartViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var viewmodel: CartViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_cart, container, false)

        val auth = Firebase.auth
        viewmodel.getFoodsCart(auth.currentUser!!.email!!)
        viewmodel.foodCartList.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                if(it.isEmpty()){
                    binding.confirmCart.visibility = View.GONE
                }else{
                    binding.emptyTextView.visibility = View.GONE
                    binding.confirmCart.visibility = View.VISIBLE
                    var total = 0
                    for(food in it){
                        total += food.price * food.orderAmount
                    }
                    binding.confirmCart.text = "Confirm Cart - ${total} $"
                }
                val adapter = FoodCartAdapter(requireContext(),it, viewmodel,auth)
                binding.adapter = adapter
            }
        })
        binding.confirmCart.setOnClickListener {

            lifecycleScope.launch{
                binding.emptyTextView.visibility = View.GONE
                binding.confirmCart.visibility=View.GONE
                binding.recyclerView3.visibility=View.GONE
                binding.animationConfirmed.visibility = View.VISIBLE
                binding.animationConfirmed.playAnimation()
                delay(5000)
                binding.emptyTextView.visibility = View.VISIBLE
                binding.animationConfirmed.visibility = View.GONE
                delay(10000)
                showNotification()
            }
            val foodCartList = viewmodel.foodCartList.value
            if(foodCartList!=null){
                for (food in foodCartList){
                    viewmodel.deleteFood(food.cartId,auth.currentUser!!.email!!)
                }
            }


        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: CartViewModel by viewModels()
        viewmodel = tempViewModel

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
                        .navigate(R.id.homeFragment)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    fun showNotification(){
        val builder: NotificationCompat.Builder
        val notfManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(requireContext(), MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(requireContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            var channel: NotificationChannel? = notfManager.getNotificationChannel("id")
            if (channel == null){
                channel = NotificationChannel("id", "name", NotificationManager.IMPORTANCE_HIGH)
                channel.description = "description"
                notfManager.createNotificationChannel(channel)
            }
            builder = NotificationCompat.Builder(requireContext(),"id")
            builder.setContentInfo("Foodo")
                .setContentText("Your order is ready.")
                .setSmallIcon(R.drawable.cart)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        }else{
            builder = NotificationCompat.Builder(requireContext())
            builder.setContentInfo("Foodo")
                .setContentText("Your order is ready.")
                .setSmallIcon(R.drawable.cart)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .priority = Notification.PRIORITY_HIGH
        }
        notfManager.notify(100,builder.build())
    }
}