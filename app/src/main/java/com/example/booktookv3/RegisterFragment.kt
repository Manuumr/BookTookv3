package com.example.booktookv3

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.booktookv3.databinding.FragmentRegisterBinding
import java.util.Calendar

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            requireContext(),
            { _, year, month, dayOfMonth ->
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

        // gustos (los puedes usar luego, ahora mismo solo los leemos)
        val leGustaTerror = binding.cbTerror.isChecked
        val leGustaDarkRomance = binding.cbRomance.isChecked
        val leGustaComedy = binding.cbComedia.isChecked
        val leGustaLGTBIQPlus = binding.cbLGTBIQPlus.isChecked
        val leGustaNovelaHistorica = binding.cbNovelaHistoria.isChecked

        // 1) Validar campos obligatorios
        if (nombre.isEmpty() || apellidos.isEmpty() || usuario.isEmpty()
            || correo.isEmpty() || fechaNacimiento.isEmpty() || contrasena.isEmpty()
        ) {
            Toast.makeText(requireContext(), "Rellena todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        // 2) Validar contraseña
        if (!esContrasenaValida(contrasena)) {
            Toast.makeText(
                requireContext(),
                "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        // 3) Éxito
        Toast.makeText(requireContext(), "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()

        // 4) Volver atrás (al login)
        findNavController().popBackStack()
    }

    private fun esContrasenaValida(contrasena: String): Boolean {
        if (contrasena.length < 8) return false
        val tieneMayuscula = contrasena.any { it.isUpperCase() }
        val tieneNumero = contrasena.any { it.isDigit() }
        return tieneMayuscula && tieneNumero
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}