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
import androidx.core.widget.doAfterTextChanged
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

            validarInput(view as EditText,1)

            encerrarSessao()
        }


        inputNumeros = view.findViewById(R.id.qtd_num)
        inputNumeros.setOnFocusChangeListener {  view: View, b: Boolean ->

            validarInput(view as EditText,2)

            encerrarSessao()

        }

        inputResult = view.findViewById(R.id.input_res)
        inputResult.doAfterTextChanged {

            val stringinputResult = inputResult.text.toString()

            if(stringinputResult.isNotEmpty()
                && btnSessao.text.toString() == "Encerrar Sessão"
                && stringinputResult.toInt() == present.res) {

                txtInvcto.text = present.conferirRes(stringinputResult.toInt())
                inputResult.text = null

            }
        }


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

    fun validarInput(editText: EditText, valueMin: Int) {

        val editTextString = editText.text.toString()

        val vazio = editTextString.isEmpty() || editTextString.toInt() < valueMin

        if(vazio) editText.setText(valueMin.toString())

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

        if(btnSessao.text.toString() == "Iniciar Sessão") return

        service.hideSoftInputFromWindow(inputResult.windowToken,0)
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.stop()
        txtConta.text = present.getStringEndSessao()
        txtConta.isClickable = true
        btnSessao.text = "Iniciar Sessão"
        present.insertSessao()
    }

    fun iniciarSessao() {

        if(btnSessao.text.toString() == "Encerrar Sessão") return

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