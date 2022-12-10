package gr.aytn.foodapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import gr.aytn.foodapp.R
import gr.aytn.foodapp.data.model.FoodCart
import gr.aytn.foodapp.databinding.FragmentDetailBinding
import gr.aytn.foodapp.ui.viewmodel.DetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewmodel: DetailViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_detail, container, false)

        val bundle: DetailFragmentArgs by navArgs()
        val food = bundle.food

        val auth = Firebase.auth

        viewmodel.getFoodsCart(auth.currentUser!!.email!!)

        Glide.with(this).load("http://kasimadalan.pe.hu/foods/images/${food.image}")
            .override(250,250)
            .into(binding.foodDetailImage)
        binding.foodDetailName.text = food.name
        binding.foodDetailCategory.text = "Category: ${food.category}"
        binding.foodDetailPrice.text = "${food.price} $"



        var count = binding.orderCount.text.toString().toInt()
        binding.btnIncrementOrder.setOnClickListener{
            if(count<10){
                count ++
                binding.orderCount.text = (count).toString()
                binding.btnAddToCart.text = "Add to cart - ${food.price*count} $"
            }
        }
        binding.btnDecrementOrder.setOnClickListener{
            if(count>0){
                count --
                binding.orderCount.text = (count).toString()
                binding.btnAddToCart.text = "Add to cart - ${food.price*count} $"
            }
        }
        binding.btnAddToCart.setOnClickListener {
            if(count != 0){
                var sameFoodExists = false
                var sameFood: FoodCart? = null
                viewmodel.foodCartList.observe(viewLifecycleOwner, Observer {foodCartList ->
                    val sameFoodList = foodCartList.filter { it.name == food.name }
                    if(sameFoodList.isNotEmpty()){
                        sameFoodExists = true
                        sameFood = sameFoodList[0]
                    }
                })
                if(sameFoodExists){
                    if(sameFood != null){
                        viewmodel.deleteFood(sameFood!!.cartId, auth.currentUser!!.email!!)
                        viewmodel.insertFood(food.name,food.image,food.price,food.category,sameFood!!.orderAmount+count,auth.currentUser!!.email!!)
                    }
                }else{
                    viewmodel.insertFood(food.name,food.image,food.price,food.category,count,auth.currentUser!!.email!!)
                }
            }
            Navigation.findNavController(it).navigate(R.id.toHome)
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: DetailViewModel by viewModels()
        viewmodel = tempViewModel
    }
}