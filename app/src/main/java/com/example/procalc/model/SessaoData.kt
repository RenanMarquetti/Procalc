package com.example.procalc.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class SessaoData (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "data_ini") val dataIni: Date,
    @ColumnInfo(name = "data_fim") val dataFim: Date,
    @ColumnInfo(name = "qtd_contas") val qtdContas: Int,
    @ColumnInfo(name = "max_sem_errar") val maxSemErrar: Int,
    @ColumnInfo(name = "qtd_erros") val qtdErros: Int,
    @ColumnInfo(name = "operacao") val operacao: String?,
    @ColumnInfo(name = "qtd_digitos") val qtdDigitos: Int,
    @ColumnInfo(name = "qtd_numeros") val qtdNumeros: Int
    )