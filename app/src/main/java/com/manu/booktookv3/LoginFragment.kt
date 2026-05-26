package com.manu.booktookv3

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.manu.booktookv3.data.BookRepository
import com.manu.booktookv3.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

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
        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            irAlFeed()
            return
        }

        binding.btnIniciarSesion.setOnClickListener { iniciarSesion() }

        binding.btnRegistroUsuario.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnIrCredit.setOnClickListener {
            val intent = Intent(requireContext(), CreditActivity::class.java).apply {
                putExtra("EXTRA_USUARIO", "Invitado")
            }
            startActivity(intent)
        }
    }

    private fun iniciarSesion() {
        val identificador = binding.etUsuario.text?.toString()?.trim().orEmpty()
        val contrasena = binding.etContrasena.text?.toString().orEmpty()

        if (identificador.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(requireContext(), R.string.login_campos_vacios, Toast.LENGTH_SHORT).show()
            return
        }

        binding.btnIniciarSesion.isEnabled = false

        BookRepository.resolveEmailForLogin(identificador) { email, error ->
            if (email == null) {
                binding.btnIniciarSesion.isEnabled = true
                Toast.makeText(
                    requireContext(),
                    error ?: getString(R.string.login_error_generico),
                    Toast.LENGTH_LONG
                ).show()
                return@resolveEmailForLogin
            }

            auth.signInWithEmailAndPassword(email, contrasena)
                .addOnCompleteListener { task ->
                    binding.btnIniciarSesion.isEnabled = true
                    if (task.isSuccessful) {
                        irAlFeed()
                    } else {
                        val detalle = task.exception?.localizedMessage
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.login_error_detalle, detalle ?: "Credenciales incorrectas"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    private fun irAlFeed() {
        BookRepository.loadUserData {
            findNavController().navigate(R.id.action_loginFragment_to_feedFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
