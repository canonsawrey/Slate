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
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class ListViewModel(app: Application): AndroidViewModel(app) {

    var onListItemClick: ((ListItem, Pair<View, String>) -> Unit)? = null
    private var list = mutableListOf<ListItem>()
    private val db = AppDatabase.getInstance(app.applicationContext)

    fun model(): ObservableTransformer<Action, State> {
        return ObservableTransformer { upstream ->
            upstream.flatMap { action ->
                when (action) {
                    is Action.RetrieveList -> getList()
                    is Action.AddItem -> {
                        list.add(action.item)
                        db?.itemDao()?.insert(Util.listItemToDatabaseListItem(action.item))
                        getList()
                    }
                }
            }
        }
    }

    private fun getList(): Observable<State> {
        val listSingle = Observable.just(list)
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
            .startWith(State.Loading)
    }

//        val db = Room.databaseBuilder(getApplication(), AppDatabase::class.java, "list.db").build()
//
//        return db.itemDao().getAllItems()
//            .map { list -> list.map(this::mapToListItem) }
//            .map<State> { list ->
//                if (list.isEmpty()) {
//                    State.ListEmpty
//                } else {
//                    State.ListRetrieved(list)
//                }
//            }
//            .onErrorReturn(State::ListFailure)
//            .toObservable()
//            .startWith(State.Loading)
//    }
//
//    private fun mapToListItem(dbItem: DatabaseListItem): ListItem {
//        return ListItem(
//            dbItem.name,
//            dbItem.quantity,
//            dbItem.quantityUnit
//        )
//    }
}