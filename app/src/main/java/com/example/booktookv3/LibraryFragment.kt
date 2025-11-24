package com.example.booktookv3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.booktookv3.databinding.FragmentLibraryBinding
import androidx.recyclerview.widget.GridLayoutManager //permite mostrar los libros en forma de "estanteria" mediante una rejilla

class LibraryFragment : Fragment() {

    // ViewBinding referencia al layout fragment_library.xml
    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    private lateinit var librosAdapter: LibrosAdapter //permite filtrar libros, recargar la lista y aplicar busquedas.

    //Datos tester
    private var todosLosLibros = testerCatalog.librosDemo

    //Estado actual de los filtros
    private var filtroEstado : testerCatalog.EstadoLectura? = null
    private var textoBusqueda: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    //recyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

        //titulo: modificamos en string.xml
        binding.tvTituloBiblioteca.text = getString(R.string.titulo_biblioteca)

        //layoutManager
        val layoutManager = GridLayoutManager(requireContext(),3)
        binding.rvLibros.layoutManager = layoutManager

        //Aplicamos adapter con testerCaralog
        librosAdapter = LibrosAdapter(testerCatalog.librosDemo)
        binding.rvLibros.adapter = librosAdapter

        // Buscador de mis libros
        binding.etBuscarMisLibros.addTextChangedListener { text ->
            textoBusqueda = text?.toString().orEmpty()
            aplicarFiltros()
        }

        // Filtros por estado
        binding.btnFiltroTodos.setOnClickListener {
            filtroEstado = null
            aplicarFiltros()
        }
        binding.btnFiltroLeidos.setOnClickListener {
            filtroEstado = testerCatalog.EstadoLectura.LEIDO
            aplicarFiltros()
        }
        binding.btnFiltroLeyendo.setOnClickListener {
            filtroEstado = testerCatalog.EstadoLectura.LEYENDO
            aplicarFiltros()
        }
        binding.btnFiltroQuieroLeer.setOnClickListener {
            filtroEstado = testerCatalog.EstadoLectura.QUIERO_LEER
            aplicarFiltros()
        }

        // Aplicamos filtros una primera vez (por si hay algo predefinido)
        aplicarFiltros()
    }

    // Combina filtro por estado + texto de búsqueda
    private fun aplicarFiltros() {
        val listaFiltrada = todosLosLibros
            // 1. Filtro por estado
            .filter { libro ->
                filtroEstado == null || libro.estado == filtroEstado
            }
            // 2. Filtro por texto en título o autor
            .filter { libro ->
                textoBusqueda.isBlank() ||
                        libro.titulo.contains(textoBusqueda, ignoreCase = true) ||
                        libro.autor.contains(textoBusqueda, ignoreCase = true)
            }

        librosAdapter.updateLibros(listaFiltrada)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}