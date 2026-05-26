package com.manu.booktookv3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.manu.booktookv3.data.BookRepository
import com.manu.booktookv3.databinding.FragmentFavItemBinding
import com.manu.booktookv3.ui.BookGridLayoutHelper

class FavItemFragment : Fragment() {

    private var _binding: FragmentFavItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: LibrosAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BookGridLayoutHelper.applyGrid(binding.rvFavoritos)
        adapter = LibrosAdapter(showFavorito = true)
        binding.rvFavoritos.adapter = adapter

        adapter.onLibroClick = { libro ->
            val bundle = Bundle().apply {
                putString("volumeId", libro.volumeId)
                putBoolean("inLibrary", true)
            }
            findNavController().navigate(R.id.action_global_to_detallesFragment, bundle)
        }
        adapter.onFavoritoChanged = { volumeId, esFavorito ->
            BookRepository.updateFavorito(volumeId, esFavorito)
        }

        cargarFavoritos()
    }

    private fun cargarFavoritos() {
        val favoritos = BookRepository.getFavoritos()
        if (favoritos.isEmpty()) {
            binding.tvVacioFavoritos.visibility = View.VISIBLE
            binding.rvFavoritos.visibility = View.GONE
        } else {
            binding.tvVacioFavoritos.visibility = View.GONE
            binding.rvFavoritos.visibility = View.VISIBLE
            adapter.updateLibros(favoritos)
        }
    }

    override fun onResume() {
        super.onResume()
        if (this::adapter.isInitialized) cargarFavoritos()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
