package com.example.recyclerviewtask

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recyclerviewtask.databinding.ActivityMainBinding
import com.example.recyclerviewtask.module.User
import com.example.recyclerviewtask.screens.UserDetailsFragment
import com.example.recyclerviewtask.screens.UserListFragment

class MainActivity : AppCompatActivity(), Navigator {

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

    override fun showDetails(user: User) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, UserDetailsFragment.newInstance(user.id))
            .commit()
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun toast(messageInt: Int) {
        Toast.makeText(this, R.string.user_delete, Toast.LENGTH_SHORT).show()
    }
}