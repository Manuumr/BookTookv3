package com.example.booktookv3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.booktookv3.databinding.FragmentFavItemBinding

class FavItemFragment : Fragment() {

    // ViewBinding en Fragment: se usa nullable para evitar fugas de memoria
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

        // 1) RecyclerView en formato grid (3 columnas, como en Library)
        binding.rvFavoritos.layoutManager = GridLayoutManager(requireContext(), 3)

        // 2) Creamos el adapter con una lista inicial vacía
        adapter = LibrosAdapter(mutableListOf())
        binding.rvFavoritos.adapter = adapter

        // 3) Cargamos solo los libros marcados como favoritos
        cargarFavoritos()
    }

    private fun cargarFavoritos() {
        val favoritos = testerCatalog.librosDemo.filter { it.esFavorito }

        if (favoritos.isEmpty()) {
            // No hay favoritos: mostramos mensaje y ocultamos la lista
            binding.tvVacioFavoritos.visibility = View.VISIBLE
            binding.rvFavoritos.visibility = View.GONE
        } else {
            // Hay favoritos: ocultamos mensaje y mostramos la lista
            binding.tvVacioFavoritos.visibility = View.GONE
            binding.rvFavoritos.visibility = View.VISIBLE

            // Actualizamos la lista del adapter
            adapter.updateLibros(favoritos)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refrescamos la lista al retomar el fragment.
        if (this::adapter.isInitialized) {
            cargarFavoritos()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}