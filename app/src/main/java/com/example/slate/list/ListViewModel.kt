package com.example.slate.list


import android.app.Activity
import android.app.Application
import android.view.View
import androidx.core.util.Pair
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.slate.Util
import com.example.slate.data.AppDatabase
import com.example.slate.data.DatabaseListItem
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class ListViewModel(app: Application): AndroidViewModel(app) {

    var onListItemClick: ((ListItem, Pair<View, String>) -> Unit)? = null
    private var list = mutableListOf<ListItem>()
    private val db = AppDatabase.getInstance(app.applicationContext)

    fun model(): ObservableTransformer<Action, State> {
        return ObservableTransformer { upstream ->
            upstream.flatMap { action ->
                when (action) {
                    is Action.RetrieveList -> getList()
                    is Action.AddItem -> addToList(action.item)
                }
            }
        }
    }

    private fun addToList(item: ListItem): Observable<State> {
        list.add(item)
        val comp = Completable.fromAction {db.itemDao().insert(Util.listItemToDatabaseListItem(item)) }
            .subscribeOn(Schedulers.io())
        return comp.andThen(getList())
    }

    private fun getList(): Observable<State> {
        return db.itemDao().getAllItems()
            .map { list -> list.map(this::mapToListItem) }
            .map { list ->
                if (list.isEmpty()) {
                    State.ListEmpty
                } else {
                    State.ListRetrieved(list)
                }
            }
            .subscribeOn(Schedulers.io())
            .onErrorReturn(State::ListFailure)
            .toObservable()
            .startWith(State.Loading)
    }

    private fun mapToListItem(dbItem: DatabaseListItem): ListItem {
        return ListItem(
            dbItem.name,
            dbItem.quantity,
            dbItem.quantityUnit
        )
    }
}