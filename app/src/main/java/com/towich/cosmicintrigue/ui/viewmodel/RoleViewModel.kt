package com.towich.cosmicintrigue.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.towich.cosmicintrigue.data.repository.MainRepository

class RoleViewModel(
    private val repository: MainRepository
): ViewModel() {
    fun getRole(): Boolean {
        return repository.getCurrentPlayer()?.isImposter ?: false
    }

}
