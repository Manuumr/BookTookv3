package com.manu.booktookv3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.manu.booktookv3.data.Book
import com.manu.booktookv3.databinding.ItemLibroBinding

class LibrosAdapter(
    private var libros: List<Book> = emptyList(),
    private val showFavorito: Boolean = true
) : RecyclerView.Adapter<LibrosAdapter.LibroViewHolder>() {

    var onLibroClick: ((Book) -> Unit)? = null
    var onLibroLongClick: ((Book) -> Unit)? = null
    var onFavoritoChanged: ((volumeId: String, esFavorito: Boolean) -> Unit)? = null

    inner class LibroViewHolder(val binding: ItemLibroBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibroViewHolder {
        val binding = ItemLibroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LibroViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LibroViewHolder, position: Int) {
        val libro = libros[position]
        val b = holder.binding

        b.tvTituloLibro.text = libro.titulo
        b.tvAutorLibro.text = libro.autor

        if (!libro.portadaUrl.isNullOrBlank()) {
            b.ivPortada.load(libro.portadaUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_foreground)
                error(R.drawable.ic_launcher_foreground)
            }
        } else {
            b.ivPortada.setImageResource(R.drawable.ic_launcher_foreground)
        }

        if (showFavorito) {
            b.btnFavorito.visibility = View.VISIBLE
            b.btnFavorito.setImageResource(
                if (libro.esFavorito) R.drawable.ic_star else R.drawable.ic_star_border
            )
            b.btnFavorito.setOnClickListener {
                libro.esFavorito = !libro.esFavorito
                onFavoritoChanged?.invoke(libro.volumeId, libro.esFavorito)
                notifyItemChanged(holder.bindingAdapterPosition)
            }
        } else {
            b.btnFavorito.visibility = View.GONE
        }

        b.root.setOnClickListener { onLibroClick?.invoke(libro) }
        b.root.setOnLongClickListener {
            onLibroLongClick?.invoke(libro)
            true
        }
    }

    override fun getItemCount(): Int = libros.size

    fun updateLibros(nuevaLista: List<Book>) {
        libros = nuevaLista
        notifyDataSetChanged()
    }
}
