package br.edu.utfpr.usandosqlite

import android.database.Cursor
import android.os.Bundle
import androidx.appcompat.widget.SearchView
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
        enableEdgeToEdge()

        binding = ActivityListarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banco = DatabaseHandler.getInstance(this)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.lvRegistros.emptyView = binding.tvEmptyList

        setupSearchView()
        initListView()
    }

    private fun setupSearchView() {
        binding.svBusca.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterList(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun initListView() {
        val cursor: Cursor = banco.listar()
        adapter = MeuAdapter(this, cursor)
        binding.lvRegistros.adapter = adapter
    }

    private fun filterList(filtro: String?) {
        val cursor: Cursor = if (filtro.isNullOrEmpty()) {
            banco.listar()
        } else {
            banco.listarPorNome(filtro)
        }
        adapter?.changeCursor(cursor)
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter?.cursor?.close()
    }
}
