package com.example.booktookv3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.booktookv3.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    // ViewBinding del fragment
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // "Base de datos" falsa de usuarios
    private val usuariosValidos = mapOf(
        "manu" to "1234",
        "marisma" to "marisma"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botón Iniciar Sesión
        binding.btnIniciarSesion.setOnClickListener {
            val usuario = binding.etUsuario.text?.toString()?.trim().orEmpty()
            val contrasena = binding.etContrasena.text?.toString().orEmpty()

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(requireContext(), "Rellena usuario y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val contrasenaCorrecta = usuariosValidos[usuario]

            if (contrasenaCorrecta == contrasena) {
                // Navegamos al Feed (pantalla principal)
                findNavController().navigate(R.id.feedFragment)
            } else {
                Toast.makeText(requireContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}