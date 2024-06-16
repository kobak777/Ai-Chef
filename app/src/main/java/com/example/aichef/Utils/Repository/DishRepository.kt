package com.example.aichef.Utils.Repository

import androidx.lifecycle.MutableLiveData
import com.example.aichef.Utils.Model.DishData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DishRepository(private val auth: FirebaseAuth) {
    private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance("https://ai-chef-e955f-default-rtdb.europe-west1.firebasedatabase.app/")
        .reference.child("Dish").child(auth.currentUser?.uid.toString())

    fun loadDishes(dishList: MutableLiveData<List<DishData>>) {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val _dishList: List<DishData> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(DishData::class.java)!!
                    }
                    dishList.postValue(_dishList)
                } catch (e: Exception) {
                    // Handle exception
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
