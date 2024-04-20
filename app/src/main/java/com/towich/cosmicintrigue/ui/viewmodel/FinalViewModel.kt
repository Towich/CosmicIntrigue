package com.towich.cosmicintrigue.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.towich.cosmicintrigue.data.repository.MainRepository

class FinalViewModel(): ViewModel() {
    public fun GetWinners():Boolean
    {
        TODO("Получение результата матча" +
                "мирные победили - false" +
                "импостеры победили - true" +
                "без запроса/get запрос")
    }
}