package gr.aytn.foodapp.repo

import gr.aytn.foodapp.data.datasource.FoodDataSource
import gr.aytn.foodapp.data.model.FoodCart
import javax.inject.Inject

class FoodRepository @Inject constructor(var fds: FoodDataSource) {
    suspend fun getAllFoods() = fds.getAllFoods()
    suspend fun getFoodsCart(userName:String): List<FoodCart> = fds.getFoodsCart(userName)
    suspend fun insertFood(name: String, image: String, price: Int, category: String, orderAmount:  Int, userName: String) =
        fds.insertFood(name, image, price, category, orderAmount, userName)
    suspend fun deleteFood(cartId:Int, userName:String) = fds.deleteFood(cartId, userName)

}