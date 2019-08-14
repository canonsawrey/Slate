package com.example.slate.list


import android.app.Application
import android.view.View
import androidx.core.util.Pair
import androidx.lifecycle.AndroidViewModel
import com.example.slate.util.Util
import com.example.slate.data.AppDatabase
import com.example.slate.data.DatabaseListItem
import com.example.slate.util.exhaustive
import com.example.slate.util.toBackportZonedDateTime
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers

class ListViewModel(app: Application): AndroidViewModel(app) {

    //var onListItemClick: ((ListItem, Pair<View, String>) -> Unit)? = null
    private val db = AppDatabase.getInstance(app.applicationContext)

    fun model(): ObservableTransformer<Action, State> {
        return ObservableTransformer { upstream ->
            upstream.flatMap { action ->
                when (action) {
                    is Action.RetrieveList -> getList()
                    is Action.AddItem -> addToList(action.item)
                    is Action.RemoveItem -> removeFromList(action.item)
                }.exhaustive
            }
        }
    }

    private fun addToList(item: ListItem): Observable<State> {
        val comp = Completable.fromAction {db.itemDao().insert(Util.listItemToDatabaseListItem(item)) }
            .subscribeOn(Schedulers.io())
        return comp.andThen(getList())
    }

    private fun removeFromList(item: ListItem): Observable<State> {
        val comp = Completable.fromAction {db.itemDao().remove(item.name) }
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
            dbItem.quantityUnit,
            dbItem.created.toBackportZonedDateTime(),
            null
        )
    }
}