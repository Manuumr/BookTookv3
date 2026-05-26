package com.manu.booktookv3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.manu.booktookv3.data.BookRepository
import com.manu.booktookv3.databinding.FragmentNovedadesBinding
import com.manu.booktookv3.ui.LibrosListHelper

class NovedadesFragment : Fragment() {

    private var _binding: FragmentNovedadesBinding? = null
    private val binding get() = _binding!!
    private var cargando = false
    private var omitirProximoResume = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNovedadesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        omitirProximoResume = true
        binding.tvNovedadesSubtitulo.text = getString(R.string.novedades_recientes_sub)
        cargarNovedades()
    }

    override fun onResume() {
        super.onResume()
        if (omitirProximoResume) {
            omitirProximoResume = false
            return
        }
        if (_binding != null && !cargando) {
            cargarNovedades()
        }
    }

    private fun cargarNovedades() {
        if (cargando) return
        cargando = true
        binding.progressNovedades.visibility = View.VISIBLE
        binding.listaLibros.rvLibros.visibility = View.GONE
        binding.listaLibros.tvListaVacia.visibility = View.GONE

        BookRepository.loadNovedadesRecientes { libros, error ->
            if (!isAdded || _binding == null) return@loadNovedadesRecientes
            cargando = false
            binding.progressNovedades.visibility = View.GONE

            if (error != null && libros.isEmpty()) {
                binding.listaLibros.tvListaVacia.visibility = View.VISIBLE
                binding.listaLibros.tvListaVacia.text = error
                return@loadNovedadesRecientes
            }

            LibrosListHelper.bind(
                listRoot = binding.listaLibros.root,
                libros = libros,
                navController = findNavController(),
                emptyMessage = getString(R.string.novedades_vacio),
                inLibrary = false
            )
        }
    }

    override fun onDestroyView() {
        cargando = false
        super.onDestroyView()
        _binding = null
    }
}
