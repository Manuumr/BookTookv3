package com.manu.booktookv3

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.manu.booktookv3.data.BookRepository
import com.manu.booktookv3.data.UserProfile
import com.manu.booktookv3.ui.GustosSelectionHelper
import com.manu.booktookv3.databinding.FragmentRegisterBinding
import java.util.Calendar

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

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
        auth = FirebaseAuth.getInstance()

        binding.etFechaNacimiento.setOnClickListener { mostrarDatePicker() }
        binding.btnRegistrar.setOnClickListener { registrarUsuario() }
        configurarGustos()
    }

    private var gustosCheckboxes = emptyList<android.widget.CheckBox>()

    private fun configurarGustos() {
        gustosCheckboxes = GustosSelectionHelper.bind(
            binding.layoutGustos,
            requireContext(),
            emptySet()
        )
    }

    private fun leerGustosMarcados(): List<String> =
        GustosSelectionHelper.selected(gustosCheckboxes)

    private fun mostrarDatePicker() {
        val calendario = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                binding.etFechaNacimiento.setText("%02d/%02d/%04d".format(dayOfMonth, month + 1, year))
            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun setRegistrando(registrando: Boolean) {
        if (_binding == null) return
        binding.btnRegistrar.isEnabled = !registrando
        binding.btnRegistrar.text = if (registrando) {
            getString(R.string.registro_en_progreso)
        } else {
            getString(R.string.btn_registrar)
        }
    }

    private fun runOnUiSeguro(block: () -> Unit) {
        view?.post {
            if (isAdded && _binding != null) block()
        } ?: run {
            if (isAdded && _binding != null) block()
        }
    }

    private fun registrarUsuario() {
        val nombre = binding.etNombre.text?.toString()?.trim().orEmpty()
        val apellidos = binding.etApellidos.text?.toString()?.trim().orEmpty()
        val nombreUsuario = binding.etUsuarioRegistrar.text?.toString()?.trim().orEmpty()
        val correo = binding.etCorreo.text?.toString()?.trim().orEmpty().lowercase()
        val fechaNacimiento = binding.etFechaNacimiento.text?.toString()?.trim().orEmpty()
        val contrasena = binding.etContrasenaRegistrar.text?.toString().orEmpty()

        val gustosSeleccionados = leerGustosMarcados()

        if (nombre.isEmpty() || apellidos.isEmpty() || nombreUsuario.isEmpty()
            || correo.isEmpty() || fechaNacimiento.isEmpty() || contrasena.isEmpty()
        ) {
            Toast.makeText(requireContext(), "Rellena todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(requireContext(), "Introduce un correo válido", Toast.LENGTH_SHORT).show()
            return
        }

        if (!esContrasenaValida(contrasena)) {
            Toast.makeText(
                requireContext(),
                "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        setRegistrando(true)

        auth.createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                if (!isAdded) return@addOnCompleteListener

                if (!task.isSuccessful) {
                    runOnUiSeguro {
                        setRegistrando(false)
                        Toast.makeText(
                            requireContext(),
                            "No se ha podido registrar: ${task.exception?.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    return@addOnCompleteListener
                }

                val usuarioFirebase = auth.currentUser
                if (usuarioFirebase == null) {
                    runOnUiSeguro {
                        setRegistrando(false)
                        Toast.makeText(requireContext(), R.string.registro_error_sesion, Toast.LENGTH_LONG).show()
                    }
                    return@addOnCompleteListener
                }

                val profile = UserProfile(
                    uid = usuarioFirebase.uid,
                    nombre = nombre,
                    apellidos = apellidos,
                    nombreUsuario = nombreUsuario,
                    email = correo,
                    gustos = gustosSeleccionados,
                    fechaNacimiento = fechaNacimiento
                )

                usuarioFirebase.updateProfile(
                    UserProfileChangeRequest.Builder()
                        .setDisplayName("$nombre $apellidos")
                        .build()
                )

                guardarPerfilYEntrar(profile, correo)
            }
    }

    private fun guardarPerfilYEntrar(profile: UserProfile, correo: String) {
        BookRepository.saveUserProfile(profile) { ok ->
            runOnUiSeguro {
                setRegistrando(false)

                if (ok) {
                    BookRepository.publishActivity("Se unió a BookTook")
                    finalizarRegistro(profile, correo, firestoreOk = true)
                } else {
                    BookRepository.applySessionProfile(profile)
                    Toast.makeText(
                        requireContext(),
                        R.string.registro_perfil_parcial,
                        Toast.LENGTH_LONG
                    ).show()
                    finalizarRegistro(profile, correo, firestoreOk = false)
                }
            }
        }
    }

    private fun finalizarRegistro(profile: UserProfile, correo: String, firestoreOk: Boolean) {
        if (!isAdded) return

        val mensaje = if (firestoreOk) {
            getString(R.string.registro_ok_con_correo, correo)
        } else {
            getString(R.string.registro_ok_con_correo, correo)
        }
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()

        findNavController().navigate(R.id.action_registerFragment_to_feedFragment)
        BookRepository.loadUserData { }
    }

    private fun esContrasenaValida(contrasena: String): Boolean {
        if (contrasena.length < 8) return false
        return contrasena.any { it.isUpperCase() } && contrasena.any { it.isDigit() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
