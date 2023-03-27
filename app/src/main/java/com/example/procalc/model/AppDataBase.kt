package com.example.procalc.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Conta::class,SessaoData::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDataBase: RoomDatabase() {

    abstract fun resDao(): ResDao

    companion object {
        private var INSTANCE: AppDataBase? = null

            fun getDatBase(context: Context): AppDataBase {

                return if(INSTANCE == null) {

                    synchronized(this,){
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDataBase::class.java,
                            "calculos_realizados"
                        ).build()
                    }
                    INSTANCE as AppDataBase
                } else {
                    INSTANCE as AppDataBase
                }

            }
    }
}