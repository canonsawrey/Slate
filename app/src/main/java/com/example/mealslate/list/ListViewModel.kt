package com.example.mealslate.list

import android.view.View
import androidx.core.util.Pair
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.rxkotlin.toObservable
class ListViewModel: ViewModel() {

    var onListItemClick: ((ListItem, Pair<View, String>) -> Unit)? = null

    fun model(): ObservableTransformer<Action, State> {
        return ObservableTransformer { upstream ->
            upstream.flatMap { action ->
                when (action) {
                    is Action.RetrieveList -> getList()
                }
            }
        }
    }

    private fun getList(): Observable<State> {
        val listOfListItems = listOf(
            ListItem("Carrot", null, null),
            ListItem("Chicken", null, null),
            ListItem("Pasta", 5.0, "boxes"),
            ListItem("Eggs", 3.0, "dozen")
        )
        val listSingle = Observable.just(listOfListItems)
        return listSingle
//            .map { list -> list.map(this::mapToListItem)
//            }
            .map<State> { list ->
                if (list.isEmpty()) {
                    State.ListEmpty
                } else {
                    State.ListRetrieved(list)
                }
            }
            .onErrorReturn(State::ListFailure)
            //.toObservable()
            .startWith(State.Loading)
    }
}