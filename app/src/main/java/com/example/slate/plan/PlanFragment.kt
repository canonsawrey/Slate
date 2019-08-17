package com.example.slate.plan


import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slate.R
import com.example.slate.common.list.BaseAdapter
import com.example.slate.list.ListItem
import com.example.slate.util.Util
import com.example.slate.util.toBackportZonedDateTime
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_plan.*
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_plan.shimmer
import java.util.*


class PlanFragment : Fragment(), View.OnClickListener, Consumer<State>, DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: PlanViewModel
    private val actions = PublishRelay.create<Action>()
    private val disp = CompositeDisposable()
    private lateinit var pref: SharedPreferences
    private val adapter = BaseAdapter<PlanItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = PlanViewModel(requireActivity().application)

        pref = activity!!.getSharedPreferences(Util.PREFERENCES_FILE, Context.MODE_PRIVATE)

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

        if (pref.contains(DATE_KEY)) {
            val cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, pref.getInt(YEAR_KEY, 0))
            cal.set(Calendar.MONTH, pref.getInt(MONTH_KEY, 0))
            cal.set(Calendar.DAY_OF_MONTH, pref.getInt(DATE_KEY, 0))

            dateChanged(cal, false)

            setupRecycler(cal.get(Calendar.DAY_OF_YEAR) - Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1)
            planScreen()
        }
        change_dates_text.setOnClickListener(this)
    }

    private fun setupRecycler(days: Int) {
        var day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val tiles = mutableListOf<PlanItem>()
        repeat(days) { tiles.add(PlanItem(day + it)) }
        cal_recycler.layoutManager = LinearLayoutManager(requireContext())
        cal_recycler.adapter = adapter
        adapter.submitList(tiles)
        cal_recycler.itemAnimator = DefaultItemAnimator()
    }

    private fun startScreen() {
        shimmer.isVisible = false
        cal_recycler.isVisible = false
        selected_dates_text.isVisible = true
    }

    private fun planScreen() {
        shimmer.isVisible = false
        cal_recycler.isVisible = true
        selected_dates_text.isVisible = true
    }

    override fun accept(state: State?) {
        when (state) {
            is State.DateChanged -> dateChanged(state.cal, true)
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

    private fun dateChanged(cal: Calendar,writeToPref: Boolean) {
        val day = Util.mapToDayString(cal.get(Calendar.DAY_OF_WEEK))
        val date = cal.get(Calendar.DAY_OF_MONTH)
        val month = Util.mapToMonthString(cal.get(Calendar.MONTH) + 1)
        val year = cal.get(Calendar.YEAR)

        if (writeToPref) {
            pref.edit().apply {
                putInt(YEAR_KEY, year)
                putInt(MONTH_KEY, cal.get(Calendar.MONTH))
                putInt(DATE_KEY, date)
                apply()
            }
            setupRecycler(cal.get(Calendar.DAY_OF_YEAR) - Calendar.getInstance().get(Calendar.DAY_OF_YEAR))
            planScreen()
        }

        selected_dates_text.text = resources.getString(R.string.formatted_selected_date, day, month, date, year)
    }

    companion object {
        const val DATE_KEY = "preferences_date"
        const val MONTH_KEY = "preferences_month"
        const val YEAR_KEY = "preferences_year"
    }

}
