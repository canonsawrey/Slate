package com.example.slate.util

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.time.ZoneId
import java.time.ZonedDateTime

@RunWith(JUnit4::class)
class DateConversionTest {

    private val testDateTimeA = ZonedDateTime.of(2000, 1, 1, 1,
        1, 1, 1, ZoneId.systemDefault()).truncateAfterSeconds()

    private val testDateTimeB = ZonedDateTime.of(2000, 1, 1, 1,
        1, 1, 1, ZoneId.systemDefault()).truncateAfterSeconds()

    @Test
    fun testDateConversion() {
        assert(testDateTimeA.toEpochSecond().toBackportZonedDateTime() == testDateTimeB)
    }
}