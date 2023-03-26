package com.example.procalc.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ResDao {

    @Insert
    fun insetConta(conta: Conta)

    @Insert
    fun insetSessao(sessao: Sessao)

    @Query("SELECT * FROM Conta WHERE operacao = :Operacao")
    fun getResultByOperacaco(Operacao: String) : List<Conta>

    @Query("SELECT max(id) FROM Sessao")
    fun getIdLastSessao() : Int

    @Query("SELECT max(id) FROM Sessao WHERE operacao = :Operacao")
    fun getIdLastSessaoBySessao(Operacao: String) : Int

    @Query("SELECT * FROM Conta WHERE id_sessao = :idSessao")
    fun getConstasByIdSessao(idSessao: Int) : List<Conta>
}