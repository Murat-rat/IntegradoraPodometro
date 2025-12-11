package com.example.integradorapodometro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecorridosViewModelFactory(
    private val usuario: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecorridosViewModel::class.java)) {
            return RecorridosViewModel(usuario) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
