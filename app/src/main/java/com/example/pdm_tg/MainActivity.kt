package com.example.pdm_tg

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {
    private lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Disable night mode and force light theme for everyone.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)

        this.context = applicationContext

        // Setup navigation.
        val navHost = supportFragmentManager.findFragmentById(R.id.navigator) as NavHostFragment
        val navController = navHost.findNavController()
        val appBarConfig = AppBarConfiguration(navController.graph)

        val toolbar = findViewById<Toolbar>(R.id.appBar)

        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(navController, appBarConfig)

        // Check if the device is running Android 13 or higher and if permissions for notifications
        // have been granted.
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
            && Build.VERSION.SDK_INT >= 33
        ) {
            // If so, request them on startup.
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
        }

    }
}
