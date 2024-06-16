package com.example.aichef.Pages

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.aichef.R
import com.example.aichef.Utils.Adapter.DishAdapter
import com.example.aichef.Utils.Model.DishData
import com.example.aichef.Utils.Model.DishViewModel
import com.example.aichef.Utils.Model.DishViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import com.example.aichef.databinding.FragmentHomePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HomePageFragment : Fragment(), AddFragment.NextBtnClickListeners, DishAdapter.OnItemDeleteClickListener {
    private lateinit var databaseRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomePageBinding
    private lateinit var popUpAddFragment: AddFragment
    private lateinit var viewModel: DishViewModel
    private lateinit var dishRecyclerView: RecyclerView
    private lateinit var adapter: DishAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance("https://ai-chef-e955f-default-rtdb.europe-west1.firebasedatabase.app/").reference
            .child("Dish").child(auth.currentUser?.uid.toString())

        registerEvent()

        binding.pointsButton.setOnClickListener {
            navController.navigate(R.id.action_homePageFragment_to_pointsFragment)
        }
        binding.statButton.setOnClickListener {
            navController.navigate(R.id.action_homePageFragment_to_statFragment)
        }

        dishRecyclerView = view.findViewById(R.id.recycleView)
        dishRecyclerView.layoutManager = LinearLayoutManager(context)
        dishRecyclerView.setHasFixedSize(true)

        val factory = DishViewModelFactory(auth)
        viewModel = ViewModelProvider(this, factory).get(DishViewModel::class.java)
        viewModel.allDishes.observe(viewLifecycleOwner, Observer {
            adapter.updateDishList(it)
        })
        adapter = DishAdapter()
        dishRecyclerView.adapter = adapter
        adapter.onDeleteClickListener = this

    }

    private fun registerEvent() {
        binding.plusBtn.setOnClickListener {
            popUpAddFragment = AddFragment()
            popUpAddFragment.setListener(this)
            popUpAddFragment.show(
                childFragmentManager,
                "AddFragment"
            )
        }
    }

    override fun saveTask(currentDate: String, selectedTime: String, selectedDish: String) {
        val dishData = DishData(currentDate, selectedDish, selectedTime)
        val newDishRef = databaseRef.push()
        val dishKey = newDishRef.key
        dishData.key = dishKey

        newDishRef.setValue(dishData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Блюдо добавлено успешно!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Что-то пошло не так!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onDeleteClick(position: Int) {
        val dishIdToDelete = adapter.dishList[position].key
        if (dishIdToDelete != null) {
            databaseRef.child(dishIdToDelete).removeValue().addOnSuccessListener {
                adapter.notifyDataSetChanged()
            }.addOnFailureListener {
                Toast.makeText(context, "Что-то пошло не так!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
