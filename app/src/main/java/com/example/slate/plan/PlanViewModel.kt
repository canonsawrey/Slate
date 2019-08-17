package com.example.slate.plan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import java.util.*

class PlanViewModel(app: Application): AndroidViewModel(app) {

    fun model(): ObservableTransformer<Action, State> {
        return ObservableTransformer { upstream ->
            upstream.flatMap { action ->
                when (action) {
                    is Action.ChangeDate -> changeDate(action)
                }
            }
        }
    }


    private fun changeDate(action: Action.ChangeDate): Observable<State> {

        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.YEAR, action.year)
        cal.set(Calendar.MONTH, action.month)
        cal.set(Calendar.DAY_OF_MONTH, action.day)

        val obs: Observable<Calendar> = Observable.just(cal)

        return obs
            .map { cal ->
                if (true) {
                    State.DateChanged(cal)
                } else {
                    State.InvalidDate
                }
            }
            .subscribeOn(Schedulers.io())
            .onErrorReturn(State::DateFailure)
            .startWith(State.Loading)
    }

}