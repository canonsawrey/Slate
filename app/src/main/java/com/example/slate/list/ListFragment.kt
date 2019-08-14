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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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

class ListFragment : Fragment(), Consumer<State> {
//    @Inject
//    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: ListViewModel
    private val actions = PublishRelay.create<Action>()
    private val adapter = BaseAdapter<ListItem>()
    private val disp = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()

        val obs = actions.compose(viewModel.model())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this)
        disp.add(obs)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpButtons()
        startScreen()
        actions.accept(Action.RetrieveList)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this)[ListViewModel::class.java]
    }

    override fun accept(state: State?) {
        when(state) {
            State.Loading -> loadingScreen()

            State.ListEmpty -> {
                adapter.submitList(listOf())
                emptyScreen()
            }

            is State.ListRetrieved -> {
                addDeleteFunction(state.items)
                adapter.submitList(state.items)
                listScreen()
            }

            is State.ListFailure -> {
            }
        }
    }

    private fun addDeleteFunction(items: List<ListItem>) {
        items.forEach {listItem ->
            if (listItem.deleteFunction == null) {
                listItem.deleteFunction = {
                    println("Delete function called")
                    actions.accept(Action.RemoveItem(listItem)) }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == NEW_ITEM) {
                actions.accept(Action.AddItem(mapStringArrayToItem(data!!.getStringArrayExtra("item")!!)))
            }
        }
    }

    private fun startScreen() {
        list_recycler.isVisible = false
        shimmer.isVisible = true
        add_button.isVisible = false
        empty_text.isVisible = false
    }

    private fun loadingScreen() {
        list_recycler.isVisible = false
        shimmer.isVisible = true
        add_button.isVisible = false
        empty_text.isVisible = false
    }

    private fun emptyScreen() {
        TransitionManager.beginDelayedTransition(container, Fade(Fade.OUT))
        list_recycler.isVisible = false
        shimmer.isVisible = false
        add_button.isVisible = true
        empty_text.isVisible = true
    }

    private fun listScreen() {
        TransitionManager.beginDelayedTransition(container, Fade(Fade.OUT))
        shimmer.stopShimmer()
        shimmer.isVisible = false
        list_recycler.isVisible = true
        add_button.isVisible = true
        empty_text.isVisible = false
    }

    private fun setUpRecyclerView() {
        list_recycler.layoutManager = LinearLayoutManager(requireContext())
        list_recycler.adapter = adapter
        list_recycler.itemAnimator = null
    }

    private fun setUpButtons() {
        add_button.setOnClickListener {
            startActivityForResult(Intent(requireActivity(), AddListItemActivity::class.java), NEW_ITEM)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disp.dispose()
    }

    private fun mapStringArrayToItem(strings: kotlin.Array<String>): ListItem {
        val deleteFun: ((ListItem) -> Unit) = { actions.accept(Action.RemoveItem(it)) }

        return when (strings.size) {
            1 -> ListItem(strings[0], null, null,
                ZonedDateTime.now().truncateAfterSeconds(), deleteFun)

            2 -> ListItem(strings[0], strings[1].toDouble(), null,
                ZonedDateTime.now().truncateAfterSeconds(), deleteFun)

            3 -> ListItem(strings[0], strings[1].toDouble(), strings[2],
                ZonedDateTime.now().truncateAfterSeconds(), deleteFun)
            else -> TODO("Implement this")
        }
    }

    companion object {
        const val NEW_ITEM = 1000
    }

}
