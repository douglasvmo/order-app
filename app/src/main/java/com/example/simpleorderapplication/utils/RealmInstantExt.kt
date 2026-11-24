package com.example.simpleorderapplication.utils

import io.realm.kotlin.types.RealmInstant
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun RealmInstant.toFormattedDate(): String {
    val instant = Instant.ofEpochSecond(epochSeconds, nanosecondsOfSecond.toLong())

    val formatter = DateTimeFormatter
        .ofPattern("dd/MM/yyyy")
        .withZone(ZoneId.systemDefault())

    return formatter.format(instant)
}