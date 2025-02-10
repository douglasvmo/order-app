package com.example.simpleorderapplication.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class Client (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "client_id")
    var id: Long = 0,
    var name: String = "",
    var phone: String = "",
    var cpf_cnpj: String = "",
    var email: String = "",
    var address: String = ""
)