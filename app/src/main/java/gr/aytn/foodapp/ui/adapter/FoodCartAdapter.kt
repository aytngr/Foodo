package gr.aytn.foodapp.ui.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import gr.aytn.foodapp.R
import gr.aytn.foodapp.data.model.Food
import gr.aytn.foodapp.data.model.FoodCart
import gr.aytn.foodapp.databinding.FoodCartDesignBinding
import gr.aytn.foodapp.databinding.FoodDesignBinding
import gr.aytn.foodapp.ui.fragment.HomeFragmentDirections
import gr.aytn.foodapp.ui.viewmodel.CartViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodCartAdapter(var context: Context, var foodList: List<FoodCart>, var viewmodel: CartViewModel, var auth: FirebaseAuth): RecyclerView.Adapter<FoodCartAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: FoodCartDesignBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: FoodCartDesignBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                                            R.layout.food_cart_design,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = foodList.get(position)
        val b = holder.binding
        b.foodName.text = food.name
        b.foodOrderCount.text = "${food.orderAmount}"
        val url = "http://kasimadalan.pe.hu/foods/images/${food.image}"
        Glide.with(context).load(url)
            .override(300,300)
            .into(b.foodImage)

        b.buttonDelete.setOnClickListener{
            val builder = AlertDialog.Builder(context);
            val alert = builder.setTitle("Delete")
                .setMessage("Delete item from cart?")
                .setPositiveButton("Yes"
                ) { dialog, p1 ->
                    Toast.makeText(context,"${food.name} deleted",Toast.LENGTH_SHORT).show()
                    viewmodel.deleteFood(food.cartId, auth.currentUser!!.email!!)
                }
                .setNegativeButton("Cancel") { dialog, p1 ->
                    dialog.cancel()
                }
                .create()
            alert.show()


        }
    }

    override fun getItemCount(): Int {
        return foodList.size
    }
}