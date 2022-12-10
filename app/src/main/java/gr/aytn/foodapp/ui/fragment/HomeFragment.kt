package gr.aytn.foodapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import gr.aytn.foodapp.R
import gr.aytn.foodapp.data.model.Food
import gr.aytn.foodapp.databinding.FragmentHomeBinding
import gr.aytn.foodapp.ui.adapter.FoodAdapter
import gr.aytn.foodapp.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() , SearchView.OnQueryTextListener{
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewmodel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false)

        binding.chipAll.isChecked = true

        var adapter: FoodAdapter
        var filter = "All"
        var sort = "Recommended"


        viewmodel.foodList.observe(viewLifecycleOwner, Observer { foodList ->

            adapter = FoodAdapter(requireContext(),foodList)
            binding.chipAll.isChecked = true
            binding.adapter = adapter

            binding.spinner2.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {


                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    when(position){
                        0-> sort = "Recommended"
                        1-> sort = "Lowest Price"
                        2-> sort = "Highest Price"
                    }

                    adapter = FoodAdapter(requireContext(),sortAndFilter(foodList,sort,filter))
                    binding.adapter = adapter
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }


            binding.chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->


                if(binding.chipAll.isChecked){
                    filter = "All"
                }else if(binding.chipMeals.isChecked){
                    filter = "Meals"
                }else if(binding.chipDesserts.isChecked){
                    filter = "Desserts"
                }else if(binding.chipDrinks.isChecked){
                    filter = "Drinks"
                }

                adapter = FoodAdapter(requireContext(),sortAndFilter(foodList,sort,filter))
                binding.adapter = adapter
            }
        })

        val searchView = binding.searchView
        searchView.setOnQueryTextListener(this)

        val filters = arrayListOf<String>("Recommended","Lowest Price","Highest Price")

        val filterAdapter: ArrayAdapter<String> =
            object: ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1,android.R.id.text1,filters){
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    view.visibility = View.GONE
                    return view
                }
            }

        binding.spinnerAdapter = filterAdapter



        return binding.root
    }

    fun sortAndFilter(list: List<Food>, sort: String, filter: String): List<Food>{
        var newList = list
        when(sort){
            "Recommended" -> newList = list
            "Lowest Price" -> newList = newList.sortedBy { it.price }
            "Highest Price" -> newList = newList.sortedByDescending { it.price }
        }
        when(filter){
            "Meals" -> newList = newList.filter{ it.category =="Meals" }
            "Desserts" -> newList = newList.filter{ it.category =="Desserts" }
            "Drinks" -> newList = newList.filter{ it.category =="Drinks" }
        }
        return newList
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: HomeViewModel by viewModels()
        viewmodel = tempViewModel

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

    }

    override fun onQueryTextSubmit(query: String): Boolean {
        CoroutineScope(Dispatchers.Main).launch {
            viewmodel.search(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        CoroutineScope(Dispatchers.Main).launch {
            viewmodel.search(newText)
        }
        return true
    }


}