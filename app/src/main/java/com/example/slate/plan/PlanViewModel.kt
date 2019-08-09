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

        val date: Date = Date(action.year, action.month, action.day)
        val obs: Observable<Date> = Observable.just(date)

        return obs
            .map { date ->
                if (true) {
                    State.DateChanged(date)
                } else {
                    State.InvalidDate
                }
            }
            .subscribeOn(Schedulers.io())
            .onErrorReturn(State::DateFailure)
            .startWith(State.Loading)
    }

}