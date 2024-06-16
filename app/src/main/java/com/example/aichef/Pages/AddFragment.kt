package com.example.aichef.Pages

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.aichef.databinding.FragmentAddBinding
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date

class AddFragment : DialogFragment() {
    private lateinit var binding: FragmentAddBinding
    private lateinit var listener : NextBtnClickListeners

    fun setListener(listener: HomePageFragment){
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backBtnAdd.setOnClickListener(){
            dismiss()
        }
        registerEvents()
    }
    @SuppressLint("SimpleDateFormat")
    fun registerEvents(){
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = sdf.format(Date())
        binding.currentDate.text = currentDate
        binding.addDealBtn.setOnClickListener {
            val selectedDish = binding.chooseDish.selectedItem.toString()
            val hour = binding.timeChoose.hour
            val minute = binding.timeChoose.minute
            val selectedTime = String.format("%02d:%02d", hour, minute)
            if (selectedDish.isNotEmpty() and selectedTime.isNotEmpty()){
                listener.saveTask(currentDate, selectedTime, selectedDish)
            }
            else{
                Toast.makeText(context, "Пусто!", Toast.LENGTH_SHORT).show()
            }
            dismiss()
        }
    }

    interface NextBtnClickListeners {
        fun saveTask(currentDate: String, selectedTime: String, selectedDish: String)
    }
}