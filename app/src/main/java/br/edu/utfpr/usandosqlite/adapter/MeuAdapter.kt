package br.edu.utfpr.usandosqlite.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import br.edu.utfpr.usandosqlite.MainActivity
import br.edu.utfpr.usandosqlite.entity.Cadastro
import br.edu.utfpr.usandosqlite.R
import br.edu.utfpr.usandosqlite.R.layout.*

class MeuAdapter(val context: Context, val registros: List<Cadastro>): BaseAdapter() {

    override fun getCount(): Int {
        return registros.size
    }

    override fun getItem(pos: Int): Any? {
        registros.get(pos).nome

        val cadastro = Cadastro(
            registros.get(pos)._id,
            registros.get(pos).nome,
            registros.get(pos).telefone,
        )

        return cadastro
    }

    override fun getItemId(pos: Int): Long {
        return registros.get(pos)._id?.toLong() ?: 0
    }

    override fun getView(
        pos: Int,
        p1: View?,
        p2: ViewGroup?
    ): View? {

        //recupera a instancia do nosso elemento lista (container com dados de cada elemento da lista)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(elemento_lista, null)

        //recupera os componentes visuais da tela
        val tvNomeElementoLista = v.findViewById<TextView>(R.id.tvNomeElementoLista)
        val tvTelefoneElementoLista = v.findViewById<TextView>(R.id.tvTelefoneElementoLista)
        val btEditarElementoLista = v.findViewById<ImageButton>(R.id.btEditarElementoLista)

        tvNomeElementoLista.text = registros.get(pos).nome
        tvTelefoneElementoLista.text = registros.get(pos).telefone

        btEditarElementoLista.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra( "cod", registros.get(pos)._id)
            intent.putExtra( "nome", registros.get(pos).nome)
            intent.putExtra( "telefone", registros.get(pos).telefone)
            context.startActivity( intent )
        }

        return v
    }

}