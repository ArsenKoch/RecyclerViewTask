package com.example.recyclerviewtask

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.recyclerviewtask.databinding.ActivityMainBinding
import com.example.recyclerviewtask.screens.UserListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, UserListFragment())
                .commit()
        }
    }
}