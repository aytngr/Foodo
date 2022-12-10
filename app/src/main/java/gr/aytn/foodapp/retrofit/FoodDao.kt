package gr.aytn.foodapp.retrofit

import gr.aytn.foodapp.data.model.CRUDResponse
import gr.aytn.foodapp.data.model.FoodCartResponse
import gr.aytn.foodapp.data.model.FoodResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface FoodDao {
    @GET("foods/getAllFoods.php")
    suspend fun getAllFoods(): FoodResponse

    @POST("foods/insertFood.php")
    @FormUrlEncoded
    suspend fun insertFood(@Field("name") name: String, @Field("image") image: String,
                            @Field("price") price: Int, @Field("category") category: String,
                            @Field("orderAmount") orderAmount: Int, @Field("userName") userName: String):CRUDResponse
    @POST("foods/getFoodsCart.php")
    @FormUrlEncoded
    suspend fun getFoodsCart(@Field("userName") userName: String): FoodCartResponse

    @POST("foods/deleteFood.php")
    @FormUrlEncoded
    suspend fun deleteFood(@Field("cartId") cartId: Int,
                           @Field("userName") userName: String): CRUDResponse
}