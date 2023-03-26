package com.example.procalc.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Conta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "operacao") val operacao: String,
    @ColumnInfo(name = "cont") val cont: String,
    @ColumnInfo(name = "res") val res: Int,
    @ColumnInfo(name = "qtdErros") val qtdErros: Int,
    @ColumnInfo(name = "time") val time: Int,
    @ColumnInfo(name = "createdDate")val createdDate: Date = Date(),
    @ColumnInfo(name = "id_sessao") val idSessao: Int
)
