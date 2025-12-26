package br.edu.utfpr.usandosqlite.adapter

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import br.edu.utfpr.usandosqlite.R
import br.edu.utfpr.usandosqlite.database.DatabaseHandler
import br.edu.utfpr.usandosqlite.entity.Cadastro


class MeuAdapter (val context: Context, val cursor: Cursor): BaseAdapter() {
    override fun getCount(): Int {
        return cursor.count
    }

    override fun getItem(pos: Int): Any? {
        cursor.moveToPosition(pos)

        val cadastro = Cadastro(
            cursor.getInt(DatabaseHandler.COL_ID.toInt()),
            cursor.getString(DatabaseHandler.COL_NOME.toInt()),
            cursor.getString(DatabaseHandler.COL_TELEFONE.toInt())
        )
        return cadastro
    }

    override fun getItemId(pos: Int): Long {
        cursor.moveToPosition(pos)
        return cursor.getInt(DatabaseHandler.COL_ID.toInt()).toLong()
    }

    override fun getView(
        pos: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View? {

        //recupera a intancia do nosso elemento lista (container com dados de cada elementoda lista)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.elemento_lista, null)

        //recupera os componentes visuais da tela
        val tvNome = view.findViewById<TextView>(R.id.tvNomeElementoLista)
        val tvTelefone = view.findViewById<TextView>(R.id.tvTelefoneElementoLista)

        cursor.moveToPosition(pos)

        tvNome.text = cursor.getString(DatabaseHandler.COL_NOME.toInt())
        tvTelefone.text = cursor.getString(DatabaseHandler.COL_TELEFONE.toInt())

        return view





    }

}