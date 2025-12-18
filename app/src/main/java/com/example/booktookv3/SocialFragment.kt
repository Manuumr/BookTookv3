package com.example.booktookv3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.booktookv3.databinding.FragmentSocialBinding

class SocialFragment : Fragment() {

    // ViewBinding
    private var _binding: FragmentSocialBinding? = null
    private val binding get() = _binding!!

    // Safe Args: args que vienen del NavController
    private val args: SocialFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSocialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Recuperamos el nombre que nos ha enviado HomeActivity
        val nombre = args.nombreUsuario  // nunca será null porque tiene defaultValue = "Usuario"

        // 2.  textos en los TextView
        binding.tvSaludoSocial.text = "Hola"
        binding.tvSubtituloSocial.text = "Bienvenido a tu espacio! :)"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}