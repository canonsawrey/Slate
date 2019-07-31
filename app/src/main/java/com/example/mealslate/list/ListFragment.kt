package com.example.mealslate.list

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealslate.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.functions.Consumer
import java.io.FileNotFoundException
import com.example.mealslate.common.list.BaseAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment(), Consumer<State> {

    private lateinit var viewModel: ListViewModel
    private val actions = PublishRelay.create<Action>()
    private val adapter = BaseAdapter<ListItem>()
    private val disp = CompositeDisposable()

    @SuppressLint("AutoDispose")
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
        //setUpButtons()
        actions.accept(Action.RetrieveList)
    }

    private fun initViewModel() {
        viewModel = ListViewModel()
        viewModel.onListItemClick = { item, headerInfo ->
            val clickEnabled = true
            if (clickEnabled) {
                val options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(requireActivity(), headerInfo)
                //ListItemDetail.launch(this, item, options.toBundle())
                Toast.makeText(activity, "Item clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun accept(state: State?) {
        when(state) {
            State.Loading -> loadingScreen()

            State.ListEmpty -> {
                adapter.submitList(listOf())
                emptyScreen()
            }

            is State.ListRetrieved -> {
                println("List retrieved, state accepted")
                adapter.submitList(state.items)
                listScreen()
            }

            is State.ListFailure -> {
                throw FileNotFoundException("Files could not be accessed. Error retrieving list.")
            }
        }
    }

    private fun loadingScreen() {
        //TODO: implement a loading animation
    }
    private fun emptyScreen() {
        //TODO: implement a empty message
    }
    private fun listScreen() {
        //TODO: implement a list view
    }

    private fun setUpRecyclerView() {
        list_recycler.layoutManager = LinearLayoutManager(requireContext())
        list_recycler.adapter = adapter
        list_recycler.itemAnimator = null
    }

    override fun onDestroy() {
        super.onDestroy()
        disp.dispose()
        println("Observable has been disposed")
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ListFragment.
         */
        @JvmStatic
        fun newInstance() =
            ListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
