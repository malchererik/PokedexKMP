package com.example.pokedexkmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.pokedexkmp.database.AppDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)


        val dbFile = applicationContext.getDatabasePath("pokedex.db")

      //Constrói o banco de dados e liga o motor SQLite do KMP
        val database = Room.databaseBuilder<AppDatabase>(
            context = applicationContext,
            name = dbFile.absolutePath
        )
            .setDriver(BundledSQLiteDriver())
            .fallbackToDestructiveMigration(true) // Recria o banco sem crashar se mudarmos tabela
            .build()

        setContent {
            App(database = database) // Passamos o banco criado para dentro do App!
        }
    }
}