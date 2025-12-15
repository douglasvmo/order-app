package com.example.simpleorderapplication.data.models


import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

class Product: RealmObject {
    @PrimaryKey
    var id: RealmUUID = RealmUUID.random()
    var description: String = ""
    var quantity: Double = 1.0
    var priceCents: Long = 0
}
