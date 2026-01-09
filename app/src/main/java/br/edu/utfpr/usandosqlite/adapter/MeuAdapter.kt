package br.edu.utfpr.usandosqlite.adapter

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import br.edu.utfpr.usandosqlite.R
import br.edu.utfpr.usandosqlite.database.DatabaseHandler

class MeuAdapter(context: Context, cursor: Cursor) : CursorAdapter(context, cursor, 0) {

    private class ViewHolder(view: View) {
        val nomeTextView: TextView = view.findViewById(R.id.tvNomeElementoLista)
        val telefoneTextView: TextView = view.findViewById(R.id.tvTelefoneElementoLista)
    }

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.elemento_lista, parent, false)
        val viewHolder = ViewHolder(view)
        view.tag = viewHolder
        return view
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val viewHolder = view.tag as ViewHolder

        val nomeIndex = cursor.getColumnIndexOrThrow(DatabaseHandler.COL_NOME)
        val telefoneIndex = cursor.getColumnIndexOrThrow(DatabaseHandler.COL_TELEFONE)

        val nome = cursor.getString(nomeIndex)
        val telefone = cursor.getString(telefoneIndex)

        viewHolder.nomeTextView.text = nome
        viewHolder.telefoneTextView.text = telefone
    }
}
