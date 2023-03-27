package com.example.procalc

import java.util.*

class Sessao(val operacao:String = "Soma",val qtdDigitos: Int, val qtdNumeros: Int) {

    lateinit var dataFim: Date

    var idSessao: Int = -1
    var qtdContas: Int = 0

    val dataIni: Date = Date()
    var maxQtdSemErrar: Int = 0
    var qtdErrosSessao: Int = 0

    fun getDataSessao(): com.example.procalc.model.SessaoData {
        return  com.example.procalc.model.SessaoData(
            id = idSessao++,
            dataIni = dataIni,
            dataFim = Date(),
            qtdContas = --qtdContas,
            maxSemErrar = maxQtdSemErrar,
            qtdErros = qtdErrosSessao,
            operacao = operacao,
            qtdDigitos = qtdDigitos,
            qtdNumeros = qtdNumeros
        )
    }

}