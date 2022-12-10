package gr.aytn.foodapp.data.model

data class FoodCart(var cartId: Int, var name: String, var image: String, var price: Int,
                    var category: String,var orderAmount: Int, var userName: String) {
}