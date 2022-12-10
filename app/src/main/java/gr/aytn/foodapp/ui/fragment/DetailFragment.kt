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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import gr.aytn.foodapp.R
import gr.aytn.foodapp.data.model.Food
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
    private lateinit var auth: FirebaseAuth
    private var count: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_detail, container, false)

        val bundle: DetailFragmentArgs by navArgs()
        val food = bundle.food

        binding.detailFragment=this

        binding.food = food

        auth = Firebase.auth

        viewmodel.getFoodsCart(auth.currentUser!!.email!!)

        Glide.with(this).load("http://kasimadalan.pe.hu/foods/images/${food.image}")
            .override(250,250)
            .into(binding.foodDetailImage)

        binding.count = 0

        return binding.root
    }

    fun addToBasketBtnClick(food: Food){
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
        Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            .navigate(R.id.toHome)
    }
    fun incrementBtnClick(){
        count = binding.orderCount.text.toString().toInt()
        Log.i("detail", count.toString())
        if(count<10){
            count ++
            Log.i("detail", count.toString())
            binding.count = count
        }
    }
    fun decrementBtnClick(){
        count = binding.orderCount.text.toString().toInt()
        if(count>0){
            count --
            binding.count = count
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: DetailViewModel by viewModels()
        viewmodel = tempViewModel
    }
}