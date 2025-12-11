package com.example.integradorapodometro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ActiveRouteViewModelFactory(
    private val usuario: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActiveRouteViewModel::class.java)) {
            return ActiveRouteViewModel(usuario) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
