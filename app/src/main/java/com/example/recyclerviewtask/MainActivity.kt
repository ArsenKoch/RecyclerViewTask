package com.example.recyclerviewtask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerviewtask.databinding.ActivityMainBinding
import com.example.recyclerviewtask.module.UserService
import com.example.recyclerviewtask.module.UsersListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter

    private val userService: UserService
        get() = (applicationContext as App).userService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()

        val layoutManager = LinearLayoutManager(this)

        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = adapter

        userService.addListener(userListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        userService.removeListener(userListener)
    }

    private val userListener: UsersListener = {
        adapter.users = it
    }
}