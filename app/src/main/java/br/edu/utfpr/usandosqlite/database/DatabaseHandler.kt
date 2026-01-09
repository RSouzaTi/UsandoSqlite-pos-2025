package br.edu.utfpr.usandosqlite.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.edu.utfpr.usandosqlite.entity.Cadastro

class DatabaseHandler private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    //Classe que cria o banco de dados
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "bdfile.sqlite"
        const val TABLE_NAME = "cadastro"
        const val COL_ID = "_id"
        const val COL_NOME = "nome"
        const val COL_TELEFONE = "telefone"

        @Volatile
        private var INSTANCE: DatabaseHandler? = null

        fun getInstance(context: Context): DatabaseHandler {
            return INSTANCE ?: synchronized(this) {
                val instance = DatabaseHandler(context)
                INSTANCE = instance
                instance
            }
        }
    }

    override fun onCreate(banco: SQLiteDatabase?) {
        banco?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_NOME TEXT, $COL_TELEFONE TEXT)")
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
        registro.put(COL_NOME, cadastro.nome)
        registro.put(COL_TELEFONE, cadastro.telefone)
        writableDatabase.insert(TABLE_NAME, null, registro)
    }

    fun alterar(cadastro: Cadastro) {
        val registro = ContentValues()
        registro.put(COL_NOME, cadastro.nome)
        registro.put(COL_TELEFONE, cadastro.telefone)
        writableDatabase.update(TABLE_NAME, registro, "$COL_ID = ?", arrayOf(cadastro._id.toString()))
    }

    fun excluir(id: Int) {
        writableDatabase.delete(TABLE_NAME, "$COL_ID = ?", arrayOf(id.toString()))
    }

    fun pesquisar(id: Int): Cadastro? {
        val cursor = readableDatabase.query(
            TABLE_NAME,
            null,
            "$COL_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        var retorno: Cadastro? = null
        cursor.use {
            if (it.moveToNext()) {
                val nome = it.getString(it.getColumnIndexOrThrow(COL_NOME))
                val telefone = it.getString(it.getColumnIndexOrThrow(COL_TELEFONE))
                retorno = Cadastro(id, nome, telefone)
            }
        }
        return retorno
    }

    fun listar(): Cursor {
        return readableDatabase.query(TABLE_NAME, null, null, null, null, null, COL_NOME)
    }

    fun listarPorNome(filtro: String): Cursor {
        val selection = "$COL_NOME LIKE ?"
        val selectionArgs = arrayOf("%${filtro}%")
        return readableDatabase.query(TABLE_NAME, null, selection, selectionArgs, null, null, COL_NOME)
    }
}
