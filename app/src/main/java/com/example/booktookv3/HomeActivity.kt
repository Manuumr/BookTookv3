package com.example.booktookv3

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.booktookv3.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inicializamos el ViewBinding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Recuperamos el NavHostFragment y el NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // 3. Recuperamos el nombre de usuario que viene de MainActivity
        val nombreUsuario = intent.getStringExtra(MainActivity.EXTRA_USUARIO) ?: "Usuario"

        // 4. Gestionamos manualmente la navegación del BottomNavigation
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.feedFragment -> {
                    navController.navigate(R.id.feedFragment)
                    true
                }
                R.id.libraryFragment -> {
                    navController.navigate(R.id.libraryFragment)
                    true
                }
                R.id.socialFragment -> {
                    // Creamos la acción global hacia SocialFragment
                    val action = NavGraphDirections.actionGlobalSocialFragment()
                    // Asignamos el argumento usando la propiedad generada por Safe Args
                    action.nombreUsuario = nombreUsuario
                    navController.navigate(action)
                    true
                }
                else -> false
            }
        }
    }
}
