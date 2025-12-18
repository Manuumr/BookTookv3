package com.example.booktookv3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.booktookv3.databinding.FragmentFeedBinding
import com.google.android.material.tabs.TabLayoutMediator

class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    // Títulos de las pestañas
    private val tabTitles = listOf("Novedades", "Descubrir", "Voy a leer")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.tvEmcabezadoFeed.text = "Bienvenido!" // más adelante: Bienvenido, {nombreUsuario}

        //  Configurar el ViewPager2 con el adapter
        val adapter = FeedPagerAdapter(this)
        binding.viewPagerFeed.adapter = adapter

        // Conectar el TabLayout con el ViewPager2
        TabLayoutMediator(binding.tabLayoutFeed, binding.viewPagerFeed) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}