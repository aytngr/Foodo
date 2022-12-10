package gr.aytn.foodapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gr.aytn.foodapp.R
import gr.aytn.foodapp.data.model.Food
import gr.aytn.foodapp.databinding.FoodDesignBinding
import gr.aytn.foodapp.databinding.FragmentCartBinding
import gr.aytn.foodapp.ui.fragment.HomeFragmentDirections

class FoodAdapter(var context: Context, var foodList: List<Food>): RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: FoodDesignBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: FoodDesignBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                                            R.layout.food_design,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = foodList.get(position)
        val b = holder.binding
        b.foodName.text = food.name
        b.foodPrice.text = "${food.price} $"
        val url = "http://kasimadalan.pe.hu/foods/images/${food.image}"
        Glide.with(context).load(url)
            .override(250,250)
            .into(b.foodImage)

        b.foodCardView.setOnClickListener {
            val transition = HomeFragmentDirections.toDetail(food)
            Navigation.findNavController(it).navigate(transition)
        }
    }

    override fun getItemCount(): Int {
        return foodList.size
    }
}