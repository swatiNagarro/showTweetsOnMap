package com.example.showtweetsonmap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.showtweetsonmap.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}