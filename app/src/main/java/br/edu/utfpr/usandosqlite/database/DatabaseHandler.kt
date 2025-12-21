package br.edu.utfpr.usandosqlite.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.edu.utfpr.usandosqlite.entity.Cadastro

class DatabaseHandler {

    class DatabeHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        companion object {
            const val DATABASE_VERSION = 1
            const val DATABASE_NAME = "bdfile.sqlite"
            const val TABLE_NAME = "cadastro"
            const val COLUMN_ID = "_id"
            const val COLUMN_NOME = "0"
            const val COLUMN_TELEFONE = "0"
        }

        override fun onCreate(banco: SQLiteDatabase?) {
            //Cria a tabela do banco de dados
            banco?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, telefone TEXT)")
        }

        override fun onUpgrade(
            banco: SQLiteDatabase?,
            oldVersion: Int,
            newVersion: Int
        ) {
            banco?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(banco)

        }

        fun inserir(cadastro: Cadastro) {
            val registro = ContentValues()
            registro.put("nome", cadastro.nome)
            registro.put("telefone", cadastro.telefone)

            //Insere o registro no banco de dados
            writableDatabase.insert(TABLE_NAME, null, registro)


        }

        fun alterar(cadastro: Cadastro) {
            val registro = ContentValues()
            registro.put("nome", cadastro.nome)
            registro.put("telefone", cadastro.telefone)
            writableDatabase.update(TABLE_NAME, registro, "_id = ${cadastro._id}", null)
        }

        fun excluir(id: Int) {
            writableDatabase.delete(TABLE_NAME, "_id = $id", null)

        }

        fun pesquisar(id: Int): Cadastro? {

            //Pesquisa o registro no banco de dados
            val registro = writableDatabase.query(
                TABLE_NAME,
                null,
                "_id = $id",
                null,
                null,
                null,
                null
            )

            var retorno: Cadastro? = null

            if (registro.moveToNext()) {

                val nome = registro.getString(1)
                val telefone = registro.getString(2)

                retorno = Cadastro(id, nome, telefone)

            }
         return retorno

        }

            fun listar(): Cursor {
                val registros = writableDatabase.query(
                        TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
                )

                return registros
                }


            }


        }


