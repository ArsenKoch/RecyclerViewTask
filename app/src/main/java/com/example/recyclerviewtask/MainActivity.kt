package com.example.recyclerviewtask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerviewtask.databinding.ActivityMainBinding
import com.example.recyclerviewtask.module.User
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

        adapter = UserAdapter(object : UserActionListener {
            override fun onUserMove(user: User, moveBy: Int) {
                userService.moveUser(user, moveBy)
            }

            override fun onUserDelete(user: User) {
                userService.deleteUser(user)
            }

            override fun onUserDetails(user: User) {
                Toast.makeText(this@MainActivity, "User: ${user.name}", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onUserFire(user: User) {
                userService.fireUser(user)
            }
        })

        val layoutManager = LinearLayoutManager(this)

        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = adapter
        val itemAnimator = binding.recyclerview.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }
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