package com.towich.cosmicintrigue.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.towich.cosmicintrigue.data.repository.MainRepository

class FinalViewModel(
    private val repository: MainRepository
) : ViewModel() {
    fun getWinners(): Boolean? {
        return repository.getWinners()
    }
}