package gr.aytn.foodapp.data.datasource

import android.util.Log
import gr.aytn.foodapp.data.model.Food
import gr.aytn.foodapp.data.model.FoodCart
import gr.aytn.foodapp.retrofit.FoodDao

class FoodDataSource(var dao: FoodDao) {
    suspend fun getAllFoods():List<Food>{
        return dao.getAllFoods().foods
    }
    suspend fun insertFood(name: String, image: String, price: Int, category: String, orderAmount:  Int, userName: String){
        dao.insertFood(name, image, price, category, orderAmount, userName)
    }
    suspend fun getFoodsCart(userName: String): List<FoodCart>{
        return dao.getFoodsCart(userName).foods_cart
    }

    suspend fun deleteFood(cartId:Int, userName:String){
        dao.deleteFood(cartId, userName)
    }
}