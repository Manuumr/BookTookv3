package com.example.booktookv3

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FeedPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    // Número de pestañas / páginas que va a manejar el ViewPager
    override fun getItemCount(): Int = 3

    // Devuelve el Fragment que se debe mostrar en cada posición del ViewPager
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NovedadesFragment()
            1 -> DescubrirFragment()
            2 -> LeerFragment()
            else -> NovedadesFragment()
        }
    }
}