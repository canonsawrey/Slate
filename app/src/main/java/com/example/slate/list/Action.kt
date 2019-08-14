package com.example.slate.list

sealed class Action {
    object RetrieveList : Action()
    data class AddItem(val item: ListItem): Action()
    data class RemoveItem(val item: ListItem): Action()
}