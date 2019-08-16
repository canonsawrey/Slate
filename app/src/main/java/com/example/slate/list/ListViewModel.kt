package com.example.slate.list


import android.app.Application
import android.view.View
import androidx.core.util.Pair
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.slate.util.Util
import com.example.slate.data.AppDatabase
import com.example.slate.data.DatabaseListItem
import com.example.slate.util.exhaustive
import com.example.slate.util.toBackportZonedDateTime
import com.example.slate.util.toListItem
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(app: Application): AndroidViewModel(app) {

    //var onListItemClick: ((ListItem, Pair<View, String>) -> Unit)? = null
    private val db = AppDatabase.getInstance(app.applicationContext)
    private var items = MutableLiveData<MutableList<ListItem>>()

    private fun retrieveDatabaseItems() {
        viewModelScope.launch {
            items.value = db.itemDao().getAllItems().map {
                it.toListItem()
            }.toMutableList()
        }
    }

    fun addToList(item: ListItem): Boolean {
        return items.value?.add(item) == null
    }

    fun removeFromList(index: Int): Boolean {
        return (items.value?.removeAt(index) != null)
    }

    private fun getList(): List<ListItem> {
        return if (items.value == null) {
            listOf()
        } else {
            items.value!!
        }
    }
}