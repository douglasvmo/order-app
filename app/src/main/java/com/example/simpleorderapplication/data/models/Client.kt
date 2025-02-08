package com.example.simpleorderapplication.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class Client (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "client_id")
    val id: Int = 0,
    val name: String,
    val phone: String?,
    var cpf_cnpj: String?,
    val email: String?,
    val address: String?
)