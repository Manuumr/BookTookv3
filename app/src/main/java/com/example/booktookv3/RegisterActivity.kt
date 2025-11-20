package com.example.booktookv3  // ajusta a tu paquete

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.booktookv3.databinding.ActivityRegisterBinding
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fecha de nacimiento: mostrar DatePicker al pulsar el campo
        binding.etFechaNacimiento.setOnClickListener {
            mostrarDatePicker()
        }

        // Botón Registrar
        binding.btnRegistrar.setOnClickListener {
            registrarUsuario()
        }
    }

    private fun mostrarDatePicker() {
        val calendario = Calendar.getInstance()
        val anio = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                // month va de 0 a 11, por eso sumamos 1
                val fechaFormateada = "%02d/%02d/%04d".format(dayOfMonth, month + 1, year)
                binding.etFechaNacimiento.setText(fechaFormateada)
            },
            anio,
            mes,
            dia
        )

        datePicker.show()
    }

    private fun registrarUsuario() {
        val nombre = binding.etNombre.text?.toString()?.trim().orEmpty()
        val apellidos = binding.etApellidos.text?.toString()?.trim().orEmpty()
        val usuario = binding.etUsuarioRegistrar.text?.toString()?.trim().orEmpty()
        val correo = binding.etCorreo.text?.toString()?.trim().orEmpty()
        val fechaNacimiento = binding.etFechaNacimiento.text?.toString()?.trim().orEmpty()
        val contrasena = binding.etContrasenaRegistrar.text?.toString()?.trim().orEmpty()

        // (Opcional) gustos:
        val leGustaTerror = binding.cbTerror.isChecked
        val leGustaDarkRomance = binding.cbRomance.isChecked
        val leGustaComedy = binding.cbComedia.isChecked
        val leGustaLGTBIQPlus = binding.cbLGTBIQPlus.isChecked
        val leGustaNovelaHistorica = binding.cbNovelaHistoria.isChecked

        // 1. Validar campos obligatorios
        if (nombre.isEmpty() || apellidos.isEmpty() || usuario.isEmpty()
            || correo.isEmpty() || fechaNacimiento.isEmpty() || contrasena.isEmpty()
        ) {
            Toast.makeText(this, "Rellena todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        // 2. Validar contraseña
        if (!esContrasenaValida(contrasena)) {
            Toast.makeText(
                this,
                "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        // 3. Mostrar mensaje de éxito
        Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()

        // 4. Volver a MainActivity
        finish()
    }

    private fun esContrasenaValida(contrasena: String): Boolean {
        if (contrasena.length < 8) return false

        val tieneMayuscula = contrasena.any { it.isUpperCase() }
        val tieneNumero = contrasena.any { it.isDigit() }

        return tieneMayuscula && tieneNumero
    }
}