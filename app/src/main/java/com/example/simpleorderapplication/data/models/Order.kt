package com.example.simpleorderapplication.data.models


import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

class Order: RealmObject {
    @PrimaryKey
    var id: RealmUUID = RealmUUID.random()

    var code: Int = 0
    var clientName: String = ""
    var clientPhone: String = ""
    var clientCode: String = ""

    var products: RealmList<Product> = realmListOf()

    var createdAt: RealmInstant = RealmInstant.now()
}
