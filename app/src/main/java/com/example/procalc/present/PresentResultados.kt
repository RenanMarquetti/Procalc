package com.example.procalc.present

import android.os.Handler
import android.os.Looper
import com.example.procalc.App
import com.example.procalc.view.ResultadoFragment

class PresentResultados(val view: ResultadoFragment) {

    private val handler = Handler(Looper.getMainLooper())

    fun getResultadosLastSessao(operacao: String) {

        Thread {
            val app = view.requireActivity().application as App
            val dao = app.db.resDao()

            val idLastSessao = dao.getIdLastSessaoBySessao(operacao)

            val contas = dao.getConstasByIdSessao(idLastSessao)

            handler.post {
                view.showResults(contas)
            }

        }.start()

    }
}