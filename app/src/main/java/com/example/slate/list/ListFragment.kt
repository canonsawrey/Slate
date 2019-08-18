package com.example.slate.list

import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
//import com.uber.autodispose.android.lifecycle.scope
//import com.uber.autodispose.autoDisposable
import com.jakewharton.rxrelay2.PublishRelay
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.example.slate.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.functions.Consumer
import java.io.FileNotFoundException
import com.example.slate.common.list.BaseAdapter
import com.example.slate.data.AppDatabase
import com.example.slate.data.DatabaseListItem
import com.example.slate.list.add.AddListItemActivity
import com.example.slate.util.truncateAfterSeconds
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_list.*
import java.lang.IllegalArgumentException
import java.sql.Array
import java.time.LocalDateTime
import java.time.ZonedDateTime
import javax.inject.Inject

class ListFragment : Fragment() {
//    @Inject
//    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: ListViewModel
    private val adapter = BaseAdapter<ListItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list_recycler.isVisible = false
        loading.isVisible = true
        add_button.isVisible = false
        empty_text.isVisible = false

        setUpRecyclerView()
        setUpButtons()
        viewModel.retrieveDatabaseItems()
        viewModel.items.observe(viewLifecycleOwner, Observer { updateUI(it) })
    }

    private fun updateUI(items: List<ListItem>?) {
        when (items) {
            null -> loadingScreen()
            else -> {
                if (items.isEmpty()) {
                    emptyScreen()
                } else {
                    addRemoveFunction(items)
                    adapter.submitList(items)
                    listScreen()
                }
            }
        }
    }

    private fun addRemoveFunction(items: List<ListItem>) {
        items.forEach {
            it.onRemoveClick = {name: String, index: Int -> removeListItem(name, index)}
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this)[ListViewModel::class.java]
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == NEW_ITEM) {
                viewModel.addToList(mapStringArrayToItem(data!!.getStringArrayExtra("item")!!))
            }
        }
    }

    private fun loadingScreen() {
        list_recycler.isVisible = false
        loading.isVisible = true
        add_button.isVisible = false
        empty_text.isVisible = false
    }

    private fun emptyScreen() {
        TransitionManager.beginDelayedTransition(container, Fade(Fade.OUT))
        list_recycler.isVisible = false
        loading.isVisible = false
        add_button.isVisible = true
        empty_text.isVisible = true
    }

    private fun listScreen() {
        TransitionManager.beginDelayedTransition(container, Fade(Fade.OUT))
        loading.isVisible = false
        list_recycler.isVisible = true
        add_button.isVisible = true
        empty_text.isVisible = false
    }

    private fun setUpRecyclerView() {
        list_recycler.layoutManager = LinearLayoutManager(requireContext())
        list_recycler.adapter = adapter
        list_recycler.itemAnimator = DefaultItemAnimator()
    }

    private fun setUpButtons() {
        add_button.setOnClickListener {
            startActivityForResult(Intent(requireActivity(), AddListItemActivity::class.java), NEW_ITEM)
        }
    }

    private fun mapStringArrayToItem(strings: kotlin.Array<String>): ListItem {
        val onRemoveClick = {name: String, index: Int -> removeListItem(name, index)}

        return when (strings.size) {
            1 -> ListItem(strings[0], null, null,
                ZonedDateTime.now().truncateAfterSeconds(), onRemoveClick)

            2 -> ListItem(strings[0], strings[1].toDouble(), null,
                ZonedDateTime.now().truncateAfterSeconds(), onRemoveClick)

            3 -> ListItem(strings[0], strings[1].toDouble(), strings[2],
                ZonedDateTime.now().truncateAfterSeconds(), onRemoveClick)
            else -> TODO("Implement this")
        }
    }

    private fun removeListItem(name: String, index: Int) {
        viewModel.removeFromList(name)
    }

    companion object {
        const val NEW_ITEM = 1000
    }

}
