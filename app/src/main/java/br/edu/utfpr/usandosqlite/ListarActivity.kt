package br.edu.utfpr.usandosqlite

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.utfpr.usandosqlite.adapter.MeuAdapter
import br.edu.utfpr.usandosqlite.database.DatabaseHandler
import br.edu.utfpr.usandosqlite.databinding.ActivityListarBinding

class ListarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListarBinding
    private lateinit var banco: DatabaseHandler
    private var adapter: MeuAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //1. Ativa o modo de tela cheia

        binding = ActivityListarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banco = DatabaseHandler.getInstance(this)
        // 2. Adiciona o espaçamento para não sobrepor a barra de status
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            // Pega o tamanho das barras do sistema (topo e rodapé)
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Aplica esse tamanho como um espaçamento (padding) no container principal
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        initListView()
    }

    private fun initListView() {
        val cursor: Cursor = banco.listar()

        // The adapter will automatically handle showing the empty view
        // when the cursor is empty.
        adapter = MeuAdapter(this, cursor)
        binding.lvRegistros.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        // The adapter's cursor will be closed automatically by the activity's lifecycle management
        // when using CursorAdapter.
        adapter?.cursor?.close()
    }

    fun fabIncluirOnclick(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
