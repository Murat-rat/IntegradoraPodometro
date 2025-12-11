package com.example.integradorapodometro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ActiveRouteViewModelFactory(
    private val usuario: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActiveRouteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ActiveRouteViewModel(usuario) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
