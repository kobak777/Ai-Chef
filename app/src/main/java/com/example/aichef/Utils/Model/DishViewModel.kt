package com.example.aichef.Utils.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aichef.Utils.Repository.DishRepository
import com.google.firebase.auth.FirebaseAuth

class DishViewModel(auth: FirebaseAuth) : ViewModel() {
    private val repository: DishRepository = DishRepository(auth)
    private val _allDishes = MutableLiveData<List<DishData>>()
    val allDishes: LiveData<List<DishData>> = _allDishes

    init {
        repository.loadDishes(_allDishes)
    }
}
