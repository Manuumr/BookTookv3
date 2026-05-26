package com.manu.booktookv3

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.manu.booktookv3.data.Book
import com.manu.booktookv3.data.BookRepository
import com.manu.booktookv3.databinding.FragmentLeerBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LeerFragment : Fragment() {

    private var _binding: FragmentLeerBinding? = null
    private val binding get() = _binding!!

    private val handler = Handler(Looper.getMainLooper())
    private var tiempoAcumuladoMillis = 0L
    private var instanteInicioMillis = 0L
    private var estaCorriendo = false

    private var librosLectura: List<Book> = emptyList()
    private var libroSeleccionado: Book? = null

    private val actualizarCronometro = object : Runnable {
        override fun run() {
            if (estaCorriendo) {
                val tiempoActual = tiempoAcumuladoMillis + (System.currentTimeMillis() - instanteInicioMillis)
                binding.tvTiempoLectura.text = formatearTiempo(tiempoActual)
                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configurarSelectorLibros()
        configurarBotones()
        actualizarEstadoInicial()
    }

    override fun onResume() {
        super.onResume()
        if (_binding != null) configurarSelectorLibros()
    }

    private fun configurarSelectorLibros() {
        librosLectura = BookRepository.getLibrosParaLectura()
        val titulos = mutableListOf(getString(R.string.selecciona_libro))
        titulos.addAll(librosLectura.map { it.titulo })
        if (librosLectura.isEmpty()) {
            titulos.add(getString(R.string.anade_libros_lectura))
        }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, titulos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spLibroLectura.adapter = adapter
    }

    private fun configurarBotones() {
        binding.btnIniciar.setOnClickListener { iniciarOCContinuarLectura() }
        binding.btnPausar.setOnClickListener { pausarLectura() }
        binding.btnGuardarLectura.setOnClickListener { guardarLectura() }
        binding.btnReset.setOnClickListener { nuevaLectura() }
    }

    private fun iniciarOCContinuarLectura() {
        val pos = binding.spLibroLectura.selectedItemPosition
        if (pos <= 0 || librosLectura.isEmpty()) {
            Toast.makeText(requireContext(), R.string.selecciona_libro_lectura, Toast.LENGTH_SHORT).show()
            return
        }
        libroSeleccionado = librosLectura[pos - 1]

        if (!estaCorriendo) {
            instanteInicioMillis = System.currentTimeMillis()
            estaCorriendo = true
            binding.tvEstadoLectura.text = getString(R.string.lectura_en_curso)
            binding.btnIniciar.text = getString(R.string.continuar)
            handler.post(actualizarCronometro)
        }
    }

    private fun pausarLectura() {
        if (estaCorriendo) {
            tiempoAcumuladoMillis += System.currentTimeMillis() - instanteInicioMillis
            estaCorriendo = false
            handler.removeCallbacks(actualizarCronometro)
            binding.tvTiempoLectura.text = formatearTiempo(tiempoAcumuladoMillis)
            binding.tvEstadoLectura.text = getString(R.string.lectura_pausada)
        }
    }

    private fun guardarLectura() {
        if (estaCorriendo) pausarLectura()
        if (tiempoAcumuladoMillis <= 0L) {
            Toast.makeText(requireContext(), R.string.inicia_antes_guardar, Toast.LENGTH_SHORT).show()
            return
        }
        val libro = libroSeleccionado ?: return
        val fecha = SimpleDateFormat("EEEE d 'de' MMMM", Locale("es", "ES")).format(Date())
        val duracion = formatearDuracionRegistro(tiempoAcumuladoMillis)
        val registro = "$fecha leíste $duracion de \"${libro.titulo}\""

        BookRepository.saveReadingSession(libro.volumeId, libro.titulo, tiempoAcumuladoMillis) {
            agregarRegistro(registro)
            Toast.makeText(requireContext(), R.string.lectura_guardada, Toast.LENGTH_SHORT).show()
            nuevaLectura()
        }
    }

    private fun nuevaLectura() {
        estaCorriendo = false
        tiempoAcumuladoMillis = 0L
        libroSeleccionado = null
        handler.removeCallbacks(actualizarCronometro)
        binding.tvTiempoLectura.text = "00:00:00"
        binding.tvEstadoLectura.text = getString(R.string.nueva_lectura_lista)
        binding.btnIniciar.text = getString(R.string.iniciar)
    }

    private fun actualizarEstadoInicial() {
        binding.tvTiempoLectura.text = "00:00:00"
        binding.tvEstadoLectura.text = getString(R.string.nueva_lectura_lista)
    }

    private fun agregarRegistro(texto: String) {
        val textView = TextView(requireContext())
        textView.text = texto.replaceFirstChar { it.uppercase() }
        textView.textSize = 14f
        textView.setPadding(0, 8, 0, 8)
        binding.layoutRegistroLectura.addView(textView, 0)
    }

    private fun formatearTiempo(millis: Long): String {
        val totalSegundos = millis / 1000
        val horas = totalSegundos / 3600
        val minutos = (totalSegundos % 3600) / 60
        val segundos = totalSegundos % 60
        return "%02d:%02d:%02d".format(horas, minutos, segundos)
    }

    private fun formatearDuracionRegistro(millis: Long): String {
        val totalMinutos = (millis / 1000) / 60
        val segundos = (millis / 1000) % 60
        return when {
            totalMinutos > 0 -> "$totalMinutos minutos"
            segundos == 1L -> "1 segundo"
            else -> "$segundos segundos"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(actualizarCronometro)
        _binding = null
    }
}
