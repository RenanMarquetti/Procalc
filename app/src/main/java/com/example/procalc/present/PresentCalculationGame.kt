package com.example.procalc.present

import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import com.example.procalc.App
import com.example.procalc.model.Conta
import com.example.procalc.model.Sessao
import com.example.procalc.view.CalculationGameFragment
import kotlin.math.pow
import kotlin.random.Random
import java.lang.Thread
import java.util.*

class PresentCalculationGame(val view: CalculationGameFragment) {

    //https://github.com/lecho/hellocharts-android
    //https://github.com/PhilJay/MPAndroidChart

    internal var qtdContas: Int = 0
    internal var qtdSemErrar: Int = 0
    internal var qtdErros: Int = 0
    internal var qtdErrosSessao: Int = 0
    internal var res: Int = 0
    internal var maxQtdSemErrar: Int = 0

    private val app = view.requireActivity().application as App
    private val dao = app.db.resDao()

    private var dataIni: Date = Date()
    private var operacao: String = view.operacao.text.toString()
    private var digitos: Double = view.inputDigitos.text.toString().toDouble()
    private var numeros: String = view.inputNumeros.text.toString()
    private var idSessao: Int = 0

    @Throws(Exception::class)
    fun conferirRes(inputRes: Int): String {

        if(inputRes != res) {
            qtdSemErrar = 0
            qtdErros++
        }
        else {
            val time = (SystemClock.elapsedRealtime() - view.chronometer.base).toInt()-500

            ++qtdSemErrar

            if(qtdSemErrar > maxQtdSemErrar) maxQtdSemErrar = qtdSemErrar

            val conta = Conta(
                operacao = operacao,
                cont = view.txtConta.text.toString(),
                res = res,
                qtdErros = qtdErros,
                time = time,
                idSessao = idSessao ?: throw Exception("idSessao não encontrado"))

            Thread { dao.insetConta(conta) }.start()

            qtdErrosSessao += qtdErros
            qtdErros = 0
            getDados()
        }
        return qtdSemErrar.toString()
    }

    fun getDados() {

        var numbers = getNumerosConta().toMutableList()

        res = numbers[0]

        view.resetChronometer()

        when(operacao) {
            "Soma" -> res = numbers.sum()
            "Subtração" -> {

                numbers = numbers.sortedDescending().toMutableList()
                res = numbers[0]
                for (c in 1 until numbers.size) res -= numbers[c]
            }
            "Multiplicação" -> {

                numbers[numbers.size -1] = Random.nextInt(1, 10)

                for (c in 1 until numbers.size) res *= numbers[c]
            }
            "Divisão" -> {

                var multi = numbers[0]

                for(c in 1 until numbers.size) multi *= numbers[c]

                numbers = numbers.toMutableList()

                res = numbers[0]
                numbers[0] = multi

            }
        }

        val stringConta = getStringConta(numbers)

        if(view.txtConta.text.toString() != stringConta) {
            qtdContas++
            view.showConta(stringConta)
        }
        else getDados()

    }

    fun getStringConta(listNumbers: List<Int>): String {

        var stringConta = listNumbers[0].toString()

        var sinal = ""

        when(operacao) {
            "Soma" -> sinal = "+"
            "Subtração" -> sinal = "-"
            "Multiplicação" -> sinal = "X"
            "Divisão" -> sinal = "/"
        }

        for (c in 1 until listNumbers.size) stringConta += sinal+listNumbers[c]

        return stringConta

    }

    fun getNumerosConta(): List<Int> {

        var digitos: Double = view.inputDigitos.text.toString().toDouble()
        var numeros: Int = view.inputNumeros.text.toString().toInt()

        val qtdDigitos = 10.0.pow(digitos).toInt()

        return List(numeros) { Random.nextInt(1, qtdDigitos)}

    }

    fun insertSessao() {

        if(qtdContas == 0) return

        Log.i("inserindo sessao", idSessao.toString())

        val currentSessao = Sessao(
            id = idSessao++,
            dataIni = dataIni,
            dataFim = Date(),
            qtdContas = --qtdContas,
            maxSemErrar = maxQtdSemErrar,
            qtdErros = qtdErrosSessao,
            operacao = operacao,
            qtdDigitos = view.inputDigitos.text.toString().toInt(),
            qtdNumeros = view.inputNumeros.text.toString().toInt()
        )

        Thread{ dao.insetSessao(currentSessao) }.start()

        maxQtdSemErrar = 0
        qtdSemErrar = 0
        qtdErrosSessao = 0
        qtdContas = 0
    }

    fun getIdSessao() {
        if(idSessao == 0) Thread { idSessao = dao.getIdLastSessao() + 1 }.start()
    }

    fun getStringEndSessao() = maxQtdSemErrar.toString() + " Max Invencivel\nClick para mais detalhes"

}