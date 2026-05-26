package com.manu.booktookv3

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.manu.booktookv3.data.BookRepository
import com.manu.booktookv3.databinding.ActivityHomeBinding
import com.manu.booktookv3.ui.NotificationBadgeHelper

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BookRepository.init(applicationContext)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNav.visibility =
                if (destination.id == R.id.loginFragment || destination.id == R.id.registerFragment)
                    View.GONE
                else
                    View.VISIBLE
            if (destination.id != R.id.loginFragment && destination.id != R.id.registerFragment) {
                actualizarBadgeSocial()
            }
        }

        if (FirebaseAuth.getInstance().currentUser != null) {
            BookRepository.loadUserData { actualizarBadgeSocial() }
        }
    }

    override fun onResume() {
        super.onResume()
        if (FirebaseAuth.getInstance().currentUser != null) {
            actualizarBadgeSocial()
        }
    }

    fun actualizarBadgeSocial() {
        if (!::binding.isInitialized) return
        BookRepository.loadSocialNotificationCounts { counts ->
            NotificationBadgeHelper.applyToBottomNav(binding.bottomNav, counts.total)
        }
    }
}
