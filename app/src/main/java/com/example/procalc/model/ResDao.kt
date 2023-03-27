package com.example.procalc.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ResDao {

    @Insert
    fun insetConta(conta: Conta)

    @Insert
    fun insetSessao(SessaoData: SessaoData)

    @Query("SELECT * FROM Conta WHERE operacao = :Operacao")
    fun getResultByOperacaco(Operacao: String) : List<Conta>

    @Query("SELECT max(id) FROM SessaoData")
    fun getIdLastSessao() : Int

    @Query("SELECT max(id) FROM SessaoData WHERE operacao = :Operacao")
    fun getIdLastSessaoBySessao(Operacao: String) : Int

    @Query("SELECT * FROM Conta WHERE id_sessao = :idSessao")
    fun getConstasByIdSessao(idSessao: Int) : List<Conta>
}