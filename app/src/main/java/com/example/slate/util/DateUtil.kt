package com.example.slate.util

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

fun Date.toBackportInstant(): Instant = Instant.ofEpochSecond(time)

fun Date.toBackportZonedDateTime(): ZonedDateTime {
        return ZonedDateTime.ofInstant(toBackportInstant(), ZoneId.systemDefault())
    }

fun Long.toBackportZonedDateTime(): ZonedDateTime {
    return ZonedDateTime.ofInstant(Date(this).toBackportInstant(), ZoneId.systemDefault())
}

fun ZonedDateTime.truncateAfterSeconds(): ZonedDateTime {
    return this.toEpochSecond().toBackportZonedDateTime()
}
