package com.example.pdm_tg

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {
    private lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.context = applicationContext

        val navHost = supportFragmentManager.findFragmentById(R.id.navigator) as NavHostFragment
        val navController = navHost.findNavController()
        val appBarConfig = AppBarConfiguration(navController.graph)

        val toolbar = findViewById<Toolbar>(R.id.appBar)

        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(navController, appBarConfig)

    }
}
