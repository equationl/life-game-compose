package com.equationl

import android.app.Application

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object{
        private lateinit var instance: BaseApplication
        fun instance() = instance
    }
}