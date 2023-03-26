package com.example.procalc.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.procalc.R
import com.example.procalc.present.PresentCalculationGame

open class CalculationGameFragment(private val operacaoName: String = "Soma") : Fragment() {

    internal lateinit var chronometer : Chronometer
    internal lateinit var txtConta: TextView
    internal lateinit var inputDigitos: EditText
    internal lateinit var inputNumeros: EditText
    internal lateinit var operacao: TextView

    private lateinit var inputResult: EditText
    private lateinit var btnSessao: Button
    private lateinit var txtInvcto: TextView
    private lateinit var present: PresentCalculationGame
    private lateinit var service: InputMethodManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_calculation_game, container, false)

    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        operacao = view.findViewById(R.id.txt_operacao)
        operacao.text = operacaoName

        inputDigitos = view.findViewById(R.id.qtd_digitos)
        inputDigitos.setOnFocusChangeListener {  view: View, b: Boolean ->

            val vazio = inputDigitos.text.toString().isEmpty() || inputDigitos.text.toString().toInt() <= 0

            if(vazio) inputDigitos.setText("1")

            if(btnSessao.text.toString() == "Encerrar Sessão") encerrarSessao()
        }


        inputNumeros = view.findViewById(R.id.qtd_num)
        inputNumeros.setOnFocusChangeListener {  view: View, b: Boolean ->

            val vazio = inputNumeros.text.toString().isEmpty() || inputNumeros.text.toString().toInt() < 2

            if(vazio) inputNumeros.setText("2")

            if(btnSessao.text.toString() == "Encerrar Sessão") encerrarSessao()

        }

        inputResult = view.findViewById(R.id.input_res)
        inputResult.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {

                val stringinputResult = inputResult.text.toString()

                if(stringinputResult.isNotEmpty()
                    && btnSessao.text.toString() == "Encerrar Sessão"
                    && stringinputResult.toInt() == present.res) {

                    txtInvcto.text = present.conferirRes(stringinputResult.toInt())
                    inputResult.text = null

                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        inputResult.setOnEditorActionListener { textView , actionId, event ->

            if(inputResult.text.toString().isNotEmpty() && btnSessao.text.toString() == "Encerrar Sessão") {
                txtInvcto.text = present.conferirRes(inputResult.text.toString().toInt())
            }
            inputResult.text = null

            true
        }

        btnSessao = view.findViewById(R.id.btn_conferir)
        btnSessao.setOnClickListener {

            when(btnSessao.text.toString()) {
                "Iniciar Sessão" -> iniciarSessao()
                "Encerrar Sessão" -> encerrarSessao()
            }
        }

        txtConta = view.findViewById(R.id.txt_conta)
        txtConta.isClickable = true
        txtConta.setOnClickListener{

            if(txtConta.text.toString() == "Click aqui para iniciar o treinamento") {
                iniciarSessao()
                return@setOnClickListener
            }

            val bundle = Bundle()
            bundle.putString(ResultadoFragment.OPERACAO, operacao.text.toString())

            findNavController().navigate(R.id.to_nav_result,bundle)
        }

        present = PresentCalculationGame(this)
        present.getIdSessao()

        chronometer = view.findViewById(R.id.chronometer)

        txtInvcto = view.findViewById(R.id.txt_qtd_sem_errar)

        service = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager



    }

    override fun onStop() {
        super.onStop()
        encerrarSessao()
    }

    override fun onDestroy() {
        super.onDestroy()
        encerrarSessao()
    }

    fun encerrarSessao() {
        service.hideSoftInputFromWindow(inputResult.windowToken,0)
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.stop()
        txtConta.text = present.getStringEndSessao()
        txtConta.isClickable = true
        btnSessao.text = "Iniciar Sessão"
        present.insertSessao()
    }

    fun iniciarSessao() {

        if(inputResult.requestFocus()) service.showSoftInput(inputResult, InputMethodManager.SHOW_IMPLICIT)

        txtInvcto.text = "0"
        btnSessao.text = "Encerrar Sessão"
        chronometer.start()
        present.getDados()
        txtConta.isClickable = false
    }

    fun showConta(stringConta: String) {
        txtConta.text = stringConta
    }

    fun resetChronometer() {
        chronometer.base = SystemClock.elapsedRealtime()
    }

}