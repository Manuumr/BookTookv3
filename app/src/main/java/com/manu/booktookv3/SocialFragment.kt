package com.manu.booktookv3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.manu.booktookv3.data.BookRepository
import com.manu.booktookv3.databinding.FragmentSocialBinding
import com.manu.booktookv3.ui.NotificationBadgeHelper

class SocialFragment : Fragment() {

    private var _binding: FragmentSocialBinding? = null
    private val binding get() = _binding!!

    private lateinit var actividadAdapter: ActividadAdapter

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

        val nombre = BookRepository.displayName()
        binding.tvSaludoSocial.text = "Hola, $nombre"
        binding.tvSubtituloSocial.text = "Conecta con otros lectores"

        binding.cardAnadirAmigos.setOnClickListener {
            findNavController().navigate(R.id.action_socialFragment_to_buscarUsuariosFragment)
        }
        binding.cardGestionAmigos.setOnClickListener {
            findNavController().navigate(R.id.action_socialFragment_to_gestionAmigosFragment)
        }
        binding.cardMensajes.setOnClickListener {
            findNavController().navigate(R.id.action_socialFragment_to_listaConversacionesFragment)
        }
        binding.cardPrivacidad.setOnClickListener {
            findNavController().navigate(R.id.action_socialFragment_to_editarGustosFragment)
        }
        actualizarResumenGustos()

        binding.ivNotificaciones.setOnClickListener {
            findNavController().navigate(R.id.action_socialFragment_to_gestionAmigosFragment)
        }

        actividadAdapter = ActividadAdapter()
        binding.rvActividadSocial.layoutManager = LinearLayoutManager(requireContext())
        binding.rvActividadSocial.adapter = actividadAdapter

        binding.spAyudaRapida.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val opcion = parent?.getItemAtPosition(position).toString()
                if (opcion == "Cerrar sesión") cerrarSesion()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
    }

    override fun onResume() {
        super.onResume()
        if (_binding != null) {
            binding.tvSaludoSocial.text = "Hola, ${BookRepository.displayName()}"
            actualizarResumenGustos()
            if (this::actividadAdapter.isInitialized) cargarActividad()
            actualizarBadges()
            (activity as? HomeActivity)?.actualizarBadgeSocial()
        }
    }

    private fun actualizarResumenGustos() {
        val gustos = BookRepository.currentProfile?.gustos.orEmpty()
        binding.tvDescCardPrivacidad.text = if (gustos.isEmpty()) {
            getString(R.string.social_card_privacidad_desc)
        } else {
            gustos.joinToString(", ")
        }
    }

    private fun actualizarBadges() {
        BookRepository.loadSocialNotificationCounts { counts ->
            if (!isAdded || _binding == null) return@loadSocialNotificationCounts
            NotificationBadgeHelper.applyToTextBadge(binding.tvBadgeNotificaciones, counts.total)
            NotificationBadgeHelper.applyToTextBadge(binding.tvBadgeMensajesCard, counts.unreadMessages)
        }
    }

    private fun cargarActividad() {
        BookRepository.loadFriendsActivity { posts ->
            if (!isAdded || _binding == null) return@loadFriendsActivity
            if (posts.isEmpty()) {
                binding.tvActividadVacia.visibility = View.VISIBLE
                binding.rvActividadSocial.visibility = View.GONE
            } else {
                binding.tvActividadVacia.visibility = View.GONE
                binding.rvActividadSocial.visibility = View.VISIBLE
                actividadAdapter.updatePosts(posts)
            }
        }
    }

    private fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
        BookRepository.clearSession()

        val navOptions = NavOptions.Builder()
            .setPopUpTo(findNavController().graph.id, true)
            .build()

        findNavController().navigate(R.id.loginFragment, null, navOptions)
        Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
