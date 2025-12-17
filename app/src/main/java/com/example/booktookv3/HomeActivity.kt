package com.example.booktookv3

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.booktookv3.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1) ViewBinding: conecta esta activity con activity_home.xml
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2) NavController: es el "cerebro" que mueve los fragments del nav_graph
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // 3) Conectamos la BottomNavigation con el NavController (sin navegación manual)
        binding.bottomNav.setupWithNavController(navController)

        // 4) Ocultar la barra inferior en el login y en register
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNav.visibility =
                if (destination.id == R.id.loginFragment || destination.id == R.id.registerFragment)
                    View.GONE
                else
                    View.VISIBLE
        }
    }
}