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
                // Navegamos usando la ACTION del nav_graph (con popUpTo para no volver al login)
                findNavController().navigate(R.id.action_loginFragment_to_feedFragment)
            } else {
                Toast.makeText(requireContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegistroUsuario.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        // Botón "Créditos" -> sigue siendo Activity (se mantiene)
        binding.btnIrCredit.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}