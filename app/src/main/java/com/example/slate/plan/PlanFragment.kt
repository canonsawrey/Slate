package com.example.slate.plan


import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.core.view.isVisible
import com.example.slate.R
import com.example.slate.Util
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_plan.*
import io.reactivex.functions.Consumer
import java.util.*


class PlanFragment : Fragment(), View.OnClickListener, Consumer<State>, DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: PlanViewModel
    private val actions = PublishRelay.create<Action>()
    private val disp = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = PlanViewModel(requireActivity().application)

        val obs = actions.compose(viewModel.model())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this)
        disp.add(obs)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startScreen()
        change_dates_text.setOnClickListener(this)
    }

    private fun startScreen() {
        shimmer.isVisible = false
        selected_dates_text.isVisible = true
    }

    override fun accept(state: State?) {
        when (state) {
            is State.DateChanged -> dateChanged(state.date)
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            change_dates_text -> launchDateDialog()
        }
    }

    private fun launchDateDialog() {
        val frag = SelectDateDialog(this)
        frag.show(fragmentManager, "choose_date")
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        actions.accept(Action.ChangeDate(day, month, year))
    }

    override fun onDestroy() {
        super.onDestroy()
        disp.dispose()
    }

    private fun dateChanged(pDate: Date) {
        val day = Util.mapToDayString(pDate.day)
        val date = pDate.date
        val month = Util.mapToMonthString(pDate.month + 1)
        val year = pDate.year
        selected_dates_text.text = resources.getString(R.string.formatted_selected_date, day, month, date, year)
    }
}
