package com.example.booktookv3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.booktookv3.databinding.FragmentDetallesBinding

class DetallesFragment : Fragment() {

    private var _binding: FragmentDetallesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetallesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1) Recibimos el ISBN del libro que se pulsó en la librería
        val isbnRecibido = arguments?.getString("isbn")

        // 2) Buscamos el libro en nuestro catálogo de prueba
        val libro = testerCatalog.librosDemo.firstOrNull { it.isbn == isbnRecibido }

        // 3) Pintamos datos en pantalla (lo mínimo)
        if (libro != null) {
            binding.ivPortadaDetalle.setImageResource(libro.portadaResId)
            binding.tvTituloDetalle.text = libro.titulo
            binding.tvAutorDetalle.text = libro.autor
            binding.tvDescripcionDetalle.text = libro.descripcion
        } else {
            // Si algo va mal, mostramos un mensaje simple
            binding.tvTituloDetalle.text = "Libro no encontrado"
            binding.tvAutorDetalle.text = ""
            binding.tvDescripcionDetalle.text = ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}