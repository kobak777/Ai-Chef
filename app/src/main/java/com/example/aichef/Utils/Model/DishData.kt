package com.example.aichef.Utils.Model

data class DishData(
    val date: String,
    val selectedDish: String,
    val selectedTime: String,
    var key: String? = null
){
    constructor() : this( "", "", "", "")
}