package com.example.booktookv3  // ajusta

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.booktookv3.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistrar.setOnClickListener {
            // De momento solo recogemos los datos en variables.
            // Más adelante validaremos y los guardaremos en BD.

            val nombre = binding.etNombre.text?.toString()?.trim().orEmpty()
            val apellidos = binding.etApellidos.text?.toString()?.trim().orEmpty()
            val usuario = binding.etUsuarioRegistrar.text?.toString()?.trim().orEmpty()
            val correo = binding.etCorreo.text?.toString()?.trim().orEmpty()
            val fechaNacimiento = binding.etFechaNacimiento.text?.toString()?.trim().orEmpty()

            val leGustaTerror = binding.cbTerror.isChecked
            val leGustaDarkRomance = binding.cbRomance.isChecked
            val leGustaComedy = binding.cbComedia.isChecked
            val leGustaLGTBQPlus = binding.cbLGTBIQPlus.isChecked
            val leGustaNovelaHistoria = binding.cbNovelaHistoria.isChecked

            // TODO: aquí más adelante:
            // - validar datos
            // - guardar usuario
            // - quizá navegar a HomeActivity directamente tras el registro

            // De momento, podríamos solo cerrar la pantalla:
            finish()
        }
    }
}