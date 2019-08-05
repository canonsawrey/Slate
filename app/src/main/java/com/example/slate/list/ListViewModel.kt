package com.example.slate.list

import android.app.Application
import android.provider.ContactsContract
import android.view.View
import androidx.core.util.Pair
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.slate.data.AppDatabase
import com.example.slate.data.DatabaseListItem
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class ListViewModel: ViewModel() {

    var onListItemClick: ((ListItem, Pair<View, String>) -> Unit)? = null
    private var list = mutableListOf<ListItem>()

    fun model(): ObservableTransformer<Action, State> {
        return ObservableTransformer { upstream ->
            upstream.flatMap { action ->
                when (action) {
                    is Action.RetrieveList -> getList()
                    is Action.AddItem -> {
                        list.add(action.item)
                        println("List is " +list.size.toString() + " long")
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