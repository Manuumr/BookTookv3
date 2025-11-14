package com.example.booktookv3

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CreditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_credit)

        val mensajeCredit = findViewById<TextView>(R.id.titulo)

        //coger el nombre que viene de MainActivity
        val nombreUsuario = intent.getStringExtra(MainActivity.EXTRA_USUARIO) ?: "Usuario"

        //Mostrar el mensaje personalizado
        mensajeCredit.text = "$nombreUsuario estás usando la versión 1 de Booktook"

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}