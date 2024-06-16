package com.example.aichef.registrationUsers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.aichef.R
import com.example.aichef.Utils.Model.Users
import com.example.aichef.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: FragmentRegisterBinding
    private val emailpattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}+"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        binding.backBtnReg.setOnClickListener {
            navController.navigate(R.id.action_registerFragment2_to_loginFragment)
        }
        binding.regButton.setOnClickListener{
            val name = binding.editUsername.text.toString()
            val email = binding.emailEt.text.toString()
            val password = binding.editPassword.text.toString()
            if(name.isEmpty()||email.isEmpty()||password.isEmpty()){
                if(name.isEmpty()){
                    Toast.makeText(context, "Введите имя!", Toast.LENGTH_SHORT).show()
                }
                if(email.isEmpty()){
                    Toast.makeText(context, "Введите email!", Toast.LENGTH_SHORT).show()
                }
                if(password.isEmpty()){
                    Toast.makeText(context, "Введите пароль!!", Toast.LENGTH_SHORT).show()
                }
            }
            else if(!email.matches(emailpattern.toRegex())){
                Toast.makeText(context, "Введен некорректный email!", Toast.LENGTH_SHORT).show()
            }
            else if(password.length<=4){
                Toast.makeText(context, "Пароль должен быть больше 4 символов!", Toast.LENGTH_SHORT).show()
            }
            else{
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
                    if(it.isSuccessful){
                        val database=database.reference.child("users").child(auth.currentUser!!.uid)
                        Toast.makeText(context, "Успешно!", Toast.LENGTH_SHORT).show()
                        val users: Users = Users(name,email,auth.currentUser!!.uid)
                        if(it.isSuccessful){
                            navController.navigate(R.id.action_registerFragment2_to_homePageFragment)
                        }else{
                            Toast.makeText(context, "Что-то пошло не так, попробуйте снова!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(context, "Пользователь уже зарегистрирован!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
    }


}