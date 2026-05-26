package com.manu.booktookv3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.material.slider.Slider
import com.manu.booktookv3.data.BookRepository
import com.manu.booktookv3.data.BookReview
import com.manu.booktookv3.data.EstadoLectura
import com.manu.booktookv3.databinding.FragmentDetallesBinding
import java.util.Locale

class DetallesFragment : Fragment() {

    private var _binding: FragmentDetallesBinding? = null
    private val binding get() = _binding!!
    private val args: DetallesFragmentArgs by navArgs()

    private lateinit var opinionAdapter: OpinionAdapter

    /** false = "No valorado" (sin puntuación guardada). true = el usuario eligió nota (puede ser 0). */
    private var valoradaActiva = false
    private var puntuacionActual = 0f

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

        val inLibrary = args.inLibrary || BookRepository.isInMyLibrary(args.volumeId)
        binding.layoutAccionesBiblioteca.visibility = if (inLibrary) View.VISIBLE else View.GONE
        binding.layoutAnadirCatalogo.visibility = if (inLibrary) View.GONE else View.VISIBLE

        binding.btnEstadoLeyendo.setOnClickListener { cambiarEstado(EstadoLectura.LEYENDO) }
        binding.btnEstadoLeido.setOnClickListener { cambiarEstado(EstadoLectura.LEIDO) }
        binding.btnEstadoQuiero.setOnClickListener { cambiarEstado(EstadoLectura.QUIERO_LEER) }
        binding.btnQuitarBiblioteca.setOnClickListener { confirmarQuitar() }
        binding.btnAnadirMiBiblioteca.setOnClickListener { anadirQuieroLeer() }

        configurarOpiniones()
        cargarLibro()
        cargarOpiniones()
    }

    private fun configurarOpiniones() {
        opinionAdapter = OpinionAdapter()
        binding.rvOpiniones.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOpiniones.adapter = opinionAdapter

        mostrarNoValorado()

        binding.btnTogglePuntuacion.setOnClickListener {
            if (valoradaActiva) {
                mostrarNoValorado()
            } else {
                mostrarSliderPuntuacion()
            }
        }

        binding.sliderPuntuacion.addOnChangeListener { _: Slider, value: Float, fromUser: Boolean ->
            if (fromUser || valoradaActiva) {
                puntuacionActual = value
                valoradaActiva = true
                actualizarTextoPuntuacion(value)
            }
        }

        binding.btnPublicarOpinion.setOnClickListener { guardarOpinion() }
    }

    private fun mostrarNoValorado() {
        valoradaActiva = false
        binding.tvValoracionSeleccionada.text = getString(R.string.no_valorado)
        binding.sliderPuntuacion.visibility = View.GONE
        binding.btnTogglePuntuacion.text = getString(R.string.anadir_puntuacion)
    }

    private fun mostrarSliderPuntuacion() {
        valoradaActiva = true
        binding.sliderPuntuacion.visibility = View.VISIBLE
        binding.sliderPuntuacion.value = puntuacionActual
        actualizarTextoPuntuacion(puntuacionActual)
        binding.btnTogglePuntuacion.text = getString(R.string.quitar_puntuacion)
    }

    private fun actualizarTextoPuntuacion(value: Float) {
        binding.tvValoracionSeleccionada.text = getString(
            R.string.valoracion_seleccionada,
            String.format(Locale.getDefault(), "%.1f", value)
        )
    }

    private fun cargarLibro() {
        BookRepository.fetchBookDetail(args.volumeId) { book, error ->
            if (!isAdded || _binding == null) return@fetchBookDetail
            if (book == null) {
                Toast.makeText(requireContext(), error ?: "Libro no encontrado", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
                return@fetchBookDetail
            }
            binding.tvTituloDetalle.text = book.titulo
            binding.tvAutorDetalle.text = book.autor
            binding.tvDescripcionDetalle.text = book.descripcion.ifBlank {
                getString(R.string.sin_descripcion_libro)
            }
            if (!book.portadaUrl.isNullOrBlank()) {
                binding.ivPortadaDetalle.load(book.portadaUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_foreground)
                }
            }
        }
    }

    private fun cargarOpiniones() {
        BookRepository.loadBookReviews(args.volumeId) { amigos, _, miReview ->
            if (!isAdded || _binding == null) return@loadBookReviews

            if (miReview != null) {
                mostrarMiOpinionGuardada(miReview)
                rellenarFormularioEdicion(miReview)
            } else {
                binding.cardMiOpinionGuardada.visibility = View.GONE
            }

            opinionAdapter.updateOpiniones(amigos)
            val vacio = amigos.isEmpty()
            binding.tvSinOpinionesAmigos.visibility = if (vacio) View.VISIBLE else View.GONE
            binding.rvOpiniones.visibility = if (vacio) View.GONE else View.VISIBLE
        }
    }

    private fun mostrarMiOpinionGuardada(review: BookReview) {
        binding.cardMiOpinionGuardada.visibility = View.VISIBLE
        binding.tvMiOpinionPuntuacion.text = OpinionAdapter.formatPuntuacion(requireContext(), review)
        binding.tvMiOpinionComentario.text = review.comentario.ifBlank {
            getString(R.string.opinion_sin_comentario)
        }
    }

    private fun rellenarFormularioEdicion(review: BookReview) {
        binding.etComentarioOpinion.setText(review.comentario)
        binding.btnPublicarOpinion.text = getString(R.string.actualizar_opinion)
        if (review.valorada && review.puntuacion != null) {
            puntuacionActual = review.puntuacion
            mostrarSliderPuntuacion()
        } else {
            mostrarNoValorado()
        }
    }

    private fun guardarOpinion() {
        val comentario = binding.etComentarioOpinion.text?.toString().orEmpty()
        binding.btnPublicarOpinion.isEnabled = false

        BookRepository.saveBookReview(
            volumeId = args.volumeId,
            valorada = valoradaActiva,
            puntuacion = if (valoradaActiva) puntuacionActual else null,
            comentario = comentario
        ) { ok, error ->
            if (!isAdded || _binding == null) return@saveBookReview
            binding.btnPublicarOpinion.isEnabled = true
            if (ok) {
                Toast.makeText(requireContext(), R.string.opinion_guardada, Toast.LENGTH_SHORT).show()
                binding.btnPublicarOpinion.text = getString(R.string.actualizar_opinion)
                cargarOpiniones()
            } else {
                val msg = when (error) {
                    "opinion_requiere_contenido" -> getString(R.string.opinion_requiere_contenido)
                    else -> error ?: getString(R.string.opinion_error)
                }
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cambiarEstado(estado: EstadoLectura) {
        BookRepository.updateEstado(args.volumeId, estado) { ok ->
            if (!isAdded) return@updateEstado
            val msg = if (ok) getString(R.string.estado_guardado) else getString(R.string.error_generico)
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmarQuitar() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.quitar_de_biblioteca)
            .setMessage(R.string.confirmar_quitar_libro)
            .setPositiveButton(R.string.quitar) { _, _ ->
                BookRepository.removeFromMyLibrary(args.volumeId) { ok ->
                    if (!isAdded) return@removeFromMyLibrary
                    if (ok) {
                        Toast.makeText(requireContext(), R.string.libro_quitado, Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    }
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun anadirQuieroLeer() {
        BookRepository.fetchBookDetail(args.volumeId) { book, _ ->
            if (book == null || !isAdded) return@fetchBookDetail
            val conEstado = book.copy(estado = EstadoLectura.QUIERO_LEER)
            BookRepository.addToMyLibrary(conEstado) { ok, msg ->
                Toast.makeText(
                    requireContext(),
                    if (ok) getString(R.string.libro_anadido) else (msg ?: "Error"),
                    Toast.LENGTH_SHORT
                ).show()
                if (ok) {
                    binding.layoutAccionesBiblioteca.visibility = View.VISIBLE
                    binding.layoutAnadirCatalogo.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
