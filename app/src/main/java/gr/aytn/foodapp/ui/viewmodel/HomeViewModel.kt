package gr.aytn.foodapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.aytn.foodapp.data.model.Food
import gr.aytn.foodapp.repo.FoodRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(var repo: FoodRepository): ViewModel() {
    var foodList = MutableLiveData<List<Food>>()

    init {
        getAllFoods()
    }

    fun getAllFoods(){
        CoroutineScope(Dispatchers.Main).launch {
            foodList.value = repo.getAllFoods()
        }
    }
    suspend fun search(searchText: String){
        foodList.value = repo.getAllFoods().filter { it.name.lowercase().contains(searchText.lowercase()) }
    }
}