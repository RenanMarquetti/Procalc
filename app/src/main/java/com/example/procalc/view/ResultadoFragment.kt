package com.example.procalc.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.procalc.R
import com.example.procalc.model.Conta
import com.example.procalc.present.PresentResultados
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class ResultadoFragment: Fragment() {

    lateinit var present: PresentResultados
    lateinit var progressBar: ProgressBar
    lateinit var dropOperacoes: AutoCompleteTextView
    lateinit var txtResult: TextView
    lateinit var chart: LineChart

    companion object {
        const val OPERACAO = "Soma"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        present = PresentResultados(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_results, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.progressBar_results)

        txtResult = view.findViewById(R.id.txt_result)

        chart = view.findViewById(R.id.line_chart_1)

        dropOperacoes = view.findViewById(R.id.autoComplete_operacoes)
        dropOperacoes.setOnDismissListener { present.getResultadosLastSessao(dropOperacoes.text.toString()) }

        val OPERACAO = arguments?.getString(OPERACAO) ?: "Soma"

        val listOperacoes = resources.getStringArray(R.array.list_operacoe)
        dropOperacoes.setText(OPERACAO)
        val adapter = ArrayAdapter(view.context,android.R.layout.simple_list_item_1,listOperacoes)
        dropOperacoes.setAdapter(adapter)

        present.getResultadosLastSessao(OPERACAO)

    }

    fun showResults(results: List<Conta>) {

        val entries = ArrayList<Entry>()

        var i = 0.0f

        for (currentResult in results) entries.add(Entry(i++, currentResult.time.toFloat()))

        val dataSet = LineDataSet(entries, "Milissegundo")

        val listContas = ArrayList<String>()

        for (currentResult in results) listContas.add(currentResult.cont)

        chart.xAxis.valueFormatter = MyValueFormatter(listContas)
        chart.xAxis.granularity = 1.0f
        chart.axisRight.setDrawLabels(false)
        chart.data = LineData(dataSet)
        chart.invalidate()

        progressBar.visibility = View.GONE

        txtResult.text = "Sem Resultados até o momento"

        if (results.isEmpty()) return

        val numAcertos = results.size
        var numErros = 0
        var tempoMedio = 0

        results.forEach { cont ->
            numErros += cont.qtdErros
            tempoMedio += cont.time
        }

        val respostas = numErros + numAcertos

        val taxaAcerto: Float = (numAcertos / respostas.toFloat()) * 100
        val taxaErros = 100.0f - taxaAcerto

        tempoMedio /= numAcertos


        txtResult.text =
            "Respostas: $respostas\nAcertos: $numAcertos ($taxaAcerto%) \nErros: $numErros ($taxaErros%) \nMédia de tempo: ${tempoMedio / 1000.0} ms \nOperações por Minuto: ${String.format("%.2f",(60000.0/tempoMedio))}"

    }
}

class MyValueFormatter(values: List<String>) : ValueFormatter() {

    private val values = values

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {

        return if(value < values.size) values[value.toInt()] else ""
    }
}