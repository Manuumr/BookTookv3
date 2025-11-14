package com.example.booktookv3

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import android.widget.EditText


class MainActivity : AppCompatActivity() {

    companion object{
        const val  EXTRA_USUARIO = "EXTRA_USUARIO" //define la clave una sola vez para evitar errores de escritura
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //1. Referencias a las vistas

        val etUsuario = findViewById<EditText>(R.id.etUsuario) //enlaza codigo con activity_main.xml
        val btnIrCredit = findViewById<Button>(R.id.btnIrCredit) //enlaza codigo con activity_main.xml

        //2. Logica del botón

        btnIrCredit.setOnClickListener{ //define qué pasa cuando pulsamos
            //leer texto del usuario
            val nombre = etUsuario.text?.toString()?.trim().orEmpty()

            //pero si esta vacio, usamos este valor por defecto
            val nombreSeguro = if (nombre.isEmpty()) "Usuario" else nombre

            //Intent explicito para abrir CreditActivity
            val intent = Intent(this, CreditActivity::class.java).apply {
                putExtra(EXTRA_USUARIO, nombreSeguro)
            }

            //Lanza Credit Activity
            startActivity(intent)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}