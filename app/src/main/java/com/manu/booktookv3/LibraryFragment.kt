package com.manu.booktookv3

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.manu.booktookv3.data.Book
import com.manu.booktookv3.data.BookRepository
import com.manu.booktookv3.data.EstadoLectura
import com.manu.booktookv3.databinding.FragmentLibraryBinding
import com.manu.booktookv3.ui.BookGridLayoutHelper

class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterMiBiblioteca: LibrosAdapter
    private lateinit var adapterGranBiblioteca: LibrosAdapter

    private var filtroEstado: EstadoLectura? = null
    private var textoBusquedaMi: String = ""

    private val searchHandler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarTabs()
        configurarMiBiblioteca()
        configurarGranBiblioteca()
    }

    override fun onResume() {
        super.onResume()
        if (_binding != null) {
            aplicarFiltrosMiBiblioteca()
        }
    }

    private fun configurarTabs() {
        binding.tabBiblioteca.addTab(binding.tabBiblioteca.newTab().setText(R.string.tab_mi_biblioteca))
        binding.tabBiblioteca.addTab(binding.tabBiblioteca.newTab().setText(R.string.tab_gran_biblioteca))

        binding.tabBiblioteca.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val miBiblioteca = tab?.position == 0
                binding.panelMiBiblioteca.visibility = if (miBiblioteca) View.VISIBLE else View.GONE
                binding.panelGranBiblioteca.visibility = if (miBiblioteca) View.GONE else View.VISIBLE
                binding.btnVerFavoritos.visibility = if (miBiblioteca) View.VISIBLE else View.GONE
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })
    }

    private fun configurarMiBiblioteca() {
        adapterMiBiblioteca = LibrosAdapter(showFavorito = true)
        BookGridLayoutHelper.applyGrid(binding.rvMiBiblioteca)
        binding.rvMiBiblioteca.adapter = adapterMiBiblioteca

        adapterMiBiblioteca.onLibroClick = { libro ->
            navegarDetalle(libro.volumeId, inLibrary = true)
        }
        adapterMiBiblioteca.onFavoritoChanged = { volumeId, esFavorito ->
            BookRepository.updateFavorito(volumeId, esFavorito)
        }

        binding.etBuscarMisLibros.addTextChangedListener { text ->
            textoBusquedaMi = text?.toString().orEmpty()
            aplicarFiltrosMiBiblioteca()
        }

        binding.btnFiltroTodos.setOnClickListener { filtroEstado = null; aplicarFiltrosMiBiblioteca() }
        binding.btnFiltroLeidos.setOnClickListener {
            filtroEstado = EstadoLectura.LEIDO; aplicarFiltrosMiBiblioteca()
        }
        binding.btnFiltroLeyendo.setOnClickListener {
            filtroEstado = EstadoLectura.LEYENDO; aplicarFiltrosMiBiblioteca()
        }
        binding.btnFiltroQuieroLeer.setOnClickListener {
            filtroEstado = EstadoLectura.QUIERO_LEER; aplicarFiltrosMiBiblioteca()
        }

        binding.btnVerFavoritos.setOnClickListener {
            findNavController().navigate(R.id.action_libraryFragment_to_favItemFragment)
        }

        aplicarFiltrosMiBiblioteca()
    }

    private fun configurarGranBiblioteca() {
        adapterGranBiblioteca = LibrosAdapter(showFavorito = false)
        BookGridLayoutHelper.applyGrid(binding.rvGranBiblioteca)
        binding.rvGranBiblioteca.adapter = adapterGranBiblioteca

        adapterGranBiblioteca.onLibroLongClick = { libro ->
            mostrarMenuLista(libro)
        }

        adapterGranBiblioteca.onLibroClick = { libro ->
            if (BookRepository.isInMyLibrary(libro.volumeId)) {
                navegarDetalle(libro.volumeId, inLibrary = true)
            } else {
                anadirALista(libro, EstadoLectura.QUIERO_LEER)
            }
        }

        binding.etBuscarGranBiblioteca.addTextChangedListener { text ->
            searchRunnable?.let { searchHandler.removeCallbacks(it) }
            val query = text?.toString().orEmpty()
            searchRunnable = Runnable { buscarEnGranBiblioteca(query) }
            searchHandler.postDelayed(searchRunnable!!, 450)
        }
    }

    private fun buscarEnGranBiblioteca(query: String) {
        if (query.trim().length < 2) {
            adapterGranBiblioteca.updateLibros(emptyList())
            binding.tvGranBibliotecaVacia.visibility = View.VISIBLE
            binding.rvGranBiblioteca.visibility = View.GONE
            binding.tvGranBibliotecaVacia.text = getString(R.string.gran_biblioteca_buscar_min)
            binding.progressGranBiblioteca.visibility = View.GONE
            return
        }

        binding.progressGranBiblioteca.visibility = View.VISIBLE
        BookRepository.searchGranBiblioteca(query) { libros, error ->
            if (!isAdded || _binding == null) return@searchGranBiblioteca
            binding.progressGranBiblioteca.visibility = View.GONE
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
            adapterGranBiblioteca.updateLibros(libros)
            val vacio = libros.isEmpty()
            binding.tvGranBibliotecaVacia.visibility = if (vacio) View.VISIBLE else View.GONE
            binding.rvGranBiblioteca.visibility = if (vacio) View.GONE else View.VISIBLE
            binding.tvGranBibliotecaVacia.text = getString(R.string.gran_biblioteca_vacia)
        }
    }

    private fun mostrarMenuLista(libro: Book) {
        PopupMenu(requireContext(), binding.rvGranBiblioteca).apply {
            menu.add(0, 1, 0, getString(R.string.filtro_quiero_leer))
            menu.add(0, 2, 1, getString(R.string.filtro_leyendo))
            menu.add(0, 3, 2, getString(R.string.filtro_leidos))
            setOnMenuItemClickListener { item ->
                val estado = when (item.itemId) {
                    2 -> EstadoLectura.LEYENDO
                    3 -> EstadoLectura.LEIDO
                    else -> EstadoLectura.QUIERO_LEER
                }
                anadirALista(libro, estado)
                true
            }
            show()
        }
    }

    private fun anadirALista(libro: Book, estado: EstadoLectura) {
        val conEstado = libro.copy(estado = estado)
        BookRepository.addToMyLibrary(conEstado) { ok, msg ->
            if (!isAdded) return@addToMyLibrary
            if (ok) {
                Toast.makeText(requireContext(), R.string.libro_anadido, Toast.LENGTH_SHORT).show()
                aplicarFiltrosMiBiblioteca()
            } else {
                Toast.makeText(requireContext(), msg ?: "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun aplicarFiltrosMiBiblioteca() {
        val lista = BookRepository.getMiBiblioteca()
            .filter { filtroEstado == null || it.estado == filtroEstado }
            .filter {
                textoBusquedaMi.isBlank() ||
                    it.titulo.contains(textoBusquedaMi, ignoreCase = true) ||
                    it.autor.contains(textoBusquedaMi, ignoreCase = true)
            }
        adapterMiBiblioteca.updateLibros(lista)
        val vacio = lista.isEmpty()
        binding.tvMiBibliotecaVacia.visibility = if (vacio) View.VISIBLE else View.GONE
        binding.rvMiBiblioteca.visibility = if (vacio) View.GONE else View.VISIBLE
    }

    private fun navegarDetalle(volumeId: String, inLibrary: Boolean) {
        val bundle = Bundle().apply {
            putString("volumeId", volumeId)
            putBoolean("inLibrary", inLibrary)
        }
        findNavController().navigate(R.id.action_libraryFragment_to_detallesFragment, bundle)
    }

    override fun onDestroyView() {
        searchRunnable?.let { searchHandler.removeCallbacks(it) }
        super.onDestroyView()
        _binding = null
    }
}
