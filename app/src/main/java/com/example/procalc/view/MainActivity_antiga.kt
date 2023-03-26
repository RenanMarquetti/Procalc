package com.example.procalc.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.procalc.R
import kotlin.math.pow
import kotlin.random.Random

class MainActivity_antiga : AppCompatActivity() {

    private lateinit var inputDigitos: EditText
    private lateinit var inputNumeros: EditText
    private lateinit var txtConta: TextView
    private lateinit var inputResult: EditText
    private lateinit var btnConferir: Button
    private lateinit var operacao: AutoCompleteTextView
    private lateinit var txtInvcto: TextView
    private var qtdSemErrar: Int = 0
    private var res: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputDigitos = findViewById(R.id.qtd_digitos)
        inputNumeros = findViewById(R.id.qtd_num)
        txtConta = findViewById(R.id.txt_conta)
        inputResult = findViewById(R.id.input_res)
        btnConferir = findViewById(R.id.btn_conferir)
        //operacao = findViewById(R.id.operacao)
        txtInvcto = findViewById(R.id.txt_qtd_sem_errar)

        operacao.setOnDismissListener { getDados()}

        inputDigitos.setOnFocusChangeListener { view: View, b: Boolean ->

            val vazio = inputDigitos.text.toString().isEmpty() || inputDigitos.text.toString().toInt() <= 0

            if(vazio) inputDigitos.setText("1")

            getDados()
        }

        inputNumeros.setOnFocusChangeListener { view: View, b: Boolean ->

            val vazio = inputNumeros.text.toString().isEmpty() || inputNumeros.text.toString().toInt() <= 2

            if(vazio) inputNumeros.setText("2")

            getDados()
        }

        inputResult.setOnEditorActionListener { textView , actionId, event ->

            if(inputResult.text.toString().isEmpty()) {
                Toast.makeText(this, "Resultado Não Encontrado", Toast.LENGTH_SHORT).show()
                return@setOnEditorActionListener false
            }
            conferirRes(inputResult.text.toString().toInt())
            true
        }

        val listOperacoes = resources.getStringArray(R.array.list_operacoe)
        operacao.setText(listOperacoes.first())
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,listOperacoes)
        operacao.setAdapter(adapter)


        btnConferir.setOnClickListener {

            if(inputResult.text.toString() == "") return@setOnClickListener
            conferirRes(inputResult.text.toString().toInt())
        }

        getDados()


    }

    fun conferirRes(inputRes: Int) {

        if(inputRes != res) {
            inputResult.text = null
            qtdSemErrar = 0
            txtInvcto.text = qtdSemErrar.toString()
            Toast.makeText(this, "A conta está errada", Toast.LENGTH_SHORT).show()
        }
        else {
            txtInvcto.text = (++qtdSemErrar).toString()
            Toast.makeText(this, "Parabéns Você acertou!!!!", Toast.LENGTH_SHORT).show()
            getDados()
        }
    }

    fun getDados(){

        inputResult.text = null

        var numbers = getNumerosConta()
        val string = getStringConta(numbers)

        txtConta.setText(string)

        res = numbers[0]

        when(operacao.text.toString()) {
            "Soma" -> res = numbers.sum()
            "Subtração" -> for(c in 1 until numbers.size) res -= numbers[c]
            "Multiplicação" -> for(c in 1 until numbers.size) res *= numbers[c]
            "Divisão" -> {

                var multi = numbers[0]

                for(c in 1 until numbers.size) multi *= numbers[c]

                numbers = numbers.toMutableList()

                res = numbers[0]
                numbers[0] = multi

                txtConta.setText(getStringConta(numbers))

            }
        }

    }

    fun getStringConta(listNumbers: List<Int>): String {

        var stringConta = listNumbers[0].toString()

        var sinal: String = ""

        when(operacao.text.toString()) {
            "Soma" -> sinal = "+"
            "Subtração" -> sinal = "-"
            "Multiplicação" -> sinal = "X"
            "Divisão" -> sinal = "/"
        }

        for (c in 1 until listNumbers.size) stringConta += sinal+listNumbers[c]

        return stringConta

    }

    fun getNumerosConta(): List<Int> {

        val qtdDigitos = 10.0.pow(inputDigitos?.text.toString().toDouble() ?: 1.0).toInt()
        val qtdNumeros = inputNumeros?.text.toString().toInt() ?: 2

        return List(qtdNumeros) { Random.nextInt(1, qtdDigitos)}

    }
}
