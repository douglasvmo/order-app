package com.example.simpleorderapplication.data.models


import com.example.simpleorderapplication.domain.Order
import com.example.simpleorderapplication.domain.Product
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey
import java.time.Instant
import java.time.ZoneId

class RealmOrder: RealmObject {
    @PrimaryKey
    var id: RealmUUID = RealmUUID.random()

    var code: Int = 0
    var clientName: String = ""
    var clientPhone: String = ""
    var clientCode: String = ""

    var products: RealmList<RealmProduct> = realmListOf()

    var createdAt: RealmInstant = RealmInstant.now()


    fun toDomain() = Order(
        id = this.id.toString(),
        code = this.code.toString(),
        clientName = this.clientName,
        clientCode = this.clientCode.ifBlank { null },
        clientPhone = this.clientPhone.ifBlank { null },
        products = this.products.map {
            Product(
                id = it.id.toString(),
                description = it.description,
                quantity = it.quantity,
                priceCents = Math.toIntExact(it.priceCents)
            )
        },
        createAt = Instant.ofEpochSecond(
            this.createdAt.epochSeconds,
            this.createdAt.nanosecondsOfSecond.toLong()
        ).atZone(ZoneId.systemDefault()).toLocalDate()
    )
}
