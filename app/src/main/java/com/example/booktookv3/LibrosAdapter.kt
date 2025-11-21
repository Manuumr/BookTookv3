package com.example.booktookv3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.booktookv3.databinding.ItemLibroBinding


//Conectamos la con Adapter la lista de libros con el RecyclerView de Library fragment

class LibrosAdapter (

    private var libros: List<testerCatalog.Libro>) : RecyclerView.Adapter<LibrosAdapter.LibroViewHolder> () {

        //ViewHolder representa el layout tarjeta de libro en la lista
        inner class LibroViewHolder(val binding: ItemLibroBinding) : RecyclerView.ViewHolder(binding.root)
        //ItemLibroBindgin es la clase generada automaticamente a partir del archivo item_libro.xml, sirve para acceder a las vistas.

    //Crea las tarjetas necesarias

    override fun onCreateViewHolder(parent: ViewGroup, viewTypw: Int): LibroViewHolder {
        val binding = ItemLibroBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LibroViewHolder(binding)
    }

    //Asocia los datos de un Libro concreto a la tarjeta (rellenar la tarjeta)

    override fun onBindViewHolder(holder: LibroViewHolder, position: Int) {
        val libro = libros[position]
        holder.binding.tvTituloLibro.text = libro.titulo
        holder.binding.tvAutorLibro.text = libro.autor
        holder.binding.ivPortada.setImageResource(libro.portadaResId)

    }

    //Indica cuantos elementos hay en la lista
    override fun getItemCount(): Int = libros.size

    //Metodo auxiliar para actualizar la lisa tras un filtro o busqueda

    fun updateLibros (nuevaLista : List<testerCatalog.Libro>){
        libros = nuevaLista
        notifyDataSetChanged()
    }

}