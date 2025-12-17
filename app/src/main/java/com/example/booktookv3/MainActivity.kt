package com.example.booktookv3

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.booktookv3.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * MainActivity ahora es SOLO el contenedor de navegación.
 * - No valida usuario/contraseña.
 * - No lanza HomeActivity.
 * - Muestra/Oculta la BottomNavigation según el fragment actual.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1) Inflamos el layout de la Activity (que ahora contiene NavHost + BottomNav)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2) Obtenemos el NavController desde el NavHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // 3) Referencia a la BottomNavigationView (evita depender del nombre generado por ViewBinding)
        val bottomNav = binding.root.findViewById<BottomNavigationView>(R.id.bottom_nav)

        // 4) Conectamos el BottomNavigationView con el NavController
        //    (esto hace que al pulsar en la barra inferior navegue al fragment correspondiente)
        bottomNav.setupWithNavController(navController)

        // 4) Opción A: el BottomNav NO debe verse en LoginFragment
        //    y SÍ debe verse en el resto.
        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNav.visibility =
                if (destination.id == R.id.loginFragment) View.GONE else View.VISIBLE
        }
    }
}