package com.example.mealslate.list

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.jakewharton.rxrelay2.PublishRelay
import androidx.core.app.ActivityOptionsCompat
import com.example.mealslate.R

class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private val actions = PublishRelay.create<Action>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        actions.compose(viewModel.model())
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











    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
        } else {
            //throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
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
