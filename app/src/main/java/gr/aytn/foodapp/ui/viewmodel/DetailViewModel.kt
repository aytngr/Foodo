package gr.aytn.foodapp.ui.viewmodel

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.aytn.foodapp.data.model.Food
import gr.aytn.foodapp.data.model.FoodCart
import gr.aytn.foodapp.repo.FoodRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(var repo: FoodRepository): ViewModel()  {

    var foodCartList = MutableLiveData<List<FoodCart>>()

    fun insertFood(name: String, image: String, price: Int, category: String, orderAmount:  Int, userName: String){
        CoroutineScope(Dispatchers.Main).launch {
           repo.insertFood(name, image, price, category, orderAmount, userName)
            getFoodsCart(userName)
        }
    }

    fun getFoodsCart(userName: String){
        CoroutineScope(Dispatchers.Main).launch {
            try{
                foodCartList.value = repo.getFoodsCart(userName)
            }catch (e: Exception){
                foodCartList.value = listOf()
            }

        }
    }

    fun deleteFood(cartId:Int, userName: String){
        CoroutineScope(Dispatchers.Main).launch {
            repo.deleteFood(cartId, userName)
            getFoodsCart(userName)
        }
    }
}