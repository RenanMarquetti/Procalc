package com.example.procalc

import android.app.Application
import com.example.procalc.model.AppDataBase

class App: Application() {

    lateinit var db: AppDataBase

    override fun onCreate() {
        super.onCreate()
        db = AppDataBase.getDatBase(this)
    }
}