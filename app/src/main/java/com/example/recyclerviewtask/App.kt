package com.example.recyclerviewtask

import android.app.Application
import com.example.recyclerviewtask.module.UserService

class App : Application() {

    val userService = UserService()
}