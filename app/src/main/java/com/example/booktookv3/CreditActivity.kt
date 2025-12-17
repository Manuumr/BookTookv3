package com.example.booktookv3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.booktookv3.databinding.ActivityCreditBinding

class CreditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //coger el nombre que viene de MainActivity
        val nombreUsuario = intent.getStringExtra("EXTRA_USUARIO") ?: "Usuario"
        //Mostrar el mensaje personalizado
        binding.titulo.text = "$nombreUsuario estás usando la versión 1 de Booktook"
    }
}