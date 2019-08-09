package com.example.slate.plan

import java.util.*

sealed class State {
    data class DateChanged(val date: Date): State()
    object InvalidDate: State()
    object Loading: State()
    data class DateFailure(val error: Throwable) : State()
}