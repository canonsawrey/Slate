package com.example.mealslate.list

sealed class State {
    object Loading : State()
    object ListEmpty : State()
    data class ListRetrieved(val providers: List<ListItem>) : State()
    data class ListFailure(val error: Throwable) : State()
}