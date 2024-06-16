package com.example.aichef.registrationUsers

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.aichef.Pages.HomePageFragment
import com.example.aichef.R
import com.example.aichef.databinding.FragmentLoginBinding
import com.example.aichef.databinding.FragmentLoginBinding.*
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginBinding
    private val emailpattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}+"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)

        binding.signUpText.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerFragment2)
        }
        binding.signButton.setOnClickListener{
            val signEmail = binding.signEmail.text.toString()
            val signPassword = binding.signPassword.text.toString()
            if (signEmail.isEmpty() || signPassword.isEmpty()) {
                if(signEmail.isEmpty()){
                    Toast.makeText(context, "Введите адрес электронной почты", Toast.LENGTH_SHORT).show()
                }
                if(signPassword.isEmpty()){
                    Toast.makeText(context, "Введите пароль", Toast.LENGTH_SHORT).show()
                }
            } else if(!signEmail.matches(emailpattern.toRegex())){
                Toast.makeText(context, "Некорректный адрес электронной почты", Toast.LENGTH_SHORT).show()
            }
            else if(signPassword.length<=4){
                Toast.makeText(context, "Пароль должен быть больше 8 символов", Toast.LENGTH_SHORT).show()
            }
            else{
                loginUser(signEmail, signPassword)
            }
        }
    }
    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }
    private fun loginUser(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful)
                navController.navigate(R.id.action_loginFragment_to_homePageFragment)
            else
                Toast.makeText(context, "Что-то пошло не так!", Toast.LENGTH_SHORT).show()
        }
    }
}