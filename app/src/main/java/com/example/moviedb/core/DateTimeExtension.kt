package com.example.moviedb.core

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun String.toFormattedDate(sourceFormat: String, expectedFormat: String): String {
    val dateTime = LocalDate.parse(this, DateTimeFormatter.ofPattern(sourceFormat))

    return try {
        dateTime.format(DateTimeFormatter.ofPattern(expectedFormat))
    } catch (_: Exception) {
        this
    }
}