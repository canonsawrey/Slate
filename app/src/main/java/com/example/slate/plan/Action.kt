package com.example.slate.plan

import java.time.Month
import java.time.Year

sealed class Action {
    //data class ChangeDate(val day: Int, val month: Month, val year: Year): Action()

    data class ChangeDate(val day: Int, val month: Int, val year: Int): Action()
}