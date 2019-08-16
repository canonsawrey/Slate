package com.example.slate.list


import android.app.Application
import android.view.View
import androidx.core.util.Pair
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.slate.data.AppDatabase
import com.example.slate.data.DatabaseListItem
import com.example.slate.util.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(app: Application): AndroidViewModel(app) {

    //var onListItemClick: ((ListItem, Pair<View, String>) -> Unit)? = null
    private val db = AppDatabase.getInstance(app.applicationContext)
    private val _items = MutableLiveData<List<ListItem>>()
    val items : LiveData<List<ListItem>> = _items

    fun retrieveDatabaseItems() {
        viewModelScope.launch {
            _items.value = db.itemDao().getAllItems().map {
                it.toListItem()
            }.toMutableList()
        }
    }

    fun addToList(item: ListItem) {
        viewModelScope.launch {
            db.itemDao().insert(item.toDatabaseListItem())
            retrieveDatabaseItems()
        }
    }

    fun removeFromList(name: String) {
        viewModelScope.launch {
            db.itemDao().remove(name)
            retrieveDatabaseItems()
        }
    }
}