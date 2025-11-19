package com.example.booktookv3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.example.booktookv3.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    companion object{
        const val  EXTRA_USUARIO = "EXTRA_USUARIO" //define la clave una sola vez para evitar errores de escritura
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inicio de usuario
        binding.btnIniciarSesion.setOnClickListener {
            val nombre = binding.etUsuario.text?.toString()?.trim().orEmpty()
            val nombreSeguro = if (nombre.isEmpty()) "Usuario" else nombre

            //Acción pasar a HomeAactivity

            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra(EXTRA_USUARIO, nombreSeguro)
            }
            startActivity(intent)
        }


        //1. Referencias a las vistas

        val etUsuario = binding.etUsuario //enlaza codigo con activity_main.xml
        val btnIrCredit = binding.btnIrCredit //enlaza codigo con activity_main.xml

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

        binding.btnRegistroUsuario.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}