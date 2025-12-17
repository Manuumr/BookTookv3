package com.example.booktookv3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.booktookv3.databinding.ItemLibroBinding


//Conectamos la con Adapter la lista de libros con el RecyclerView de Library fragment

class LibrosAdapter(
    private var libros: List<testerCatalog.Libro>) : RecyclerView.Adapter<LibrosAdapter.LibroViewHolder>() {

    var onLibroClick: ((testerCatalog.Libro) -> Unit)? = null

    //ViewHolder representa el layout tarjeta de libro en la lista
    inner class LibroViewHolder(val binding: ItemLibroBinding) : RecyclerView.ViewHolder(binding.root)
    //ItemLibroBindgin es la clase generada automaticamente a partir del archivo item_libro.xml, sirve para acceder a las vistas.

    //Crea las tarjetas necesarias

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibroViewHolder {
        val binding = ItemLibroBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LibroViewHolder(binding)
    }

    //Asocia los datos de un Libro concreto a la tarjeta (rellenar la tarjeta)

    override fun onBindViewHolder(holder: LibroViewHolder, position: Int) {
        val libro = libros[position]
        val b = holder.binding

        b.tvTituloLibro.text = libro.titulo
        b.tvAutorLibro.text = libro.autor
        b.ivPortada.setImageResource(libro.portadaResId)

        // Pintamos la estrella según el estado actual
        b.btnFavorito.setImageResource(
            if (libro.esFavorito) R.drawable.ic_star else R.drawable.ic_star_border
        )

        // Al pulsar, alternamos favorito/no favorito y refrescamos SOLO esa tarjeta
        b.btnFavorito.setOnClickListener {
            libro.esFavorito = !libro.esFavorito
            notifyItemChanged(holder.bindingAdapterPosition)
        }
        b.root.setOnClickListener {
            onLibroClick?.invoke(libro)
        }
    }

    //Indica cuantos elementos hay en la lista
    override fun getItemCount(): Int = libros.size

    //Metodo auxiliar para actualizar la lisa tras un filtro o busqueda

    fun updateLibros (nuevaLista : List<testerCatalog.Libro>){
        libros = nuevaLista
        notifyDataSetChanged()
    }

}