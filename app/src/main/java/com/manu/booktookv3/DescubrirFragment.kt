package com.manu.booktookv3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.manu.booktookv3.data.BookRepository
import com.manu.booktookv3.databinding.FragmentDescubrirBinding
import com.manu.booktookv3.ui.LibrosListHelper

class DescubrirFragment : Fragment() {

    private var _binding: FragmentDescubrirBinding? = null
    private val binding get() = _binding!!
    private var cargando = false
    private var omitirProximoResume = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescubrirBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        omitirProximoResume = true
        cargarDescubrir()
    }

    override fun onResume() {
        super.onResume()
        if (omitirProximoResume) {
            omitirProximoResume = false
            return
        }
        if (_binding != null && !cargando) {
            cargarDescubrir()
        }
    }

    private fun cargarDescubrir() {
        if (cargando) return
        cargando = true
        binding.progressDescubrir.visibility = View.VISIBLE
        binding.listaLibros.rvLibros.visibility = View.GONE
        binding.listaLibros.tvListaVacia.visibility = View.GONE

        val gustos = BookRepository.currentProfile?.gustos.orEmpty()
        binding.tvDescubrirSubtitulo.text = if (gustos.isEmpty()) {
            getString(R.string.descubrir_sin_gustos)
        } else {
            getString(R.string.descubrir_para_ti, gustos.joinToString(", "))
        }

        BookRepository.loadDescubrirPersonalizado { libros, error ->
            if (!isAdded || _binding == null) return@loadDescubrirPersonalizado
            cargando = false
            binding.progressDescubrir.visibility = View.GONE

            if (error != null && libros.isEmpty()) {
                binding.listaLibros.tvListaVacia.visibility = View.VISIBLE
                binding.listaLibros.tvListaVacia.text = error
                return@loadDescubrirPersonalizado
            }

            LibrosListHelper.bind(
                listRoot = binding.listaLibros.root,
                libros = libros,
                navController = findNavController(),
                emptyMessage = getString(R.string.descubrir_vacio),
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
