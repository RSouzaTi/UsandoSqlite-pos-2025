package br.edu.utfpr.usandosqlite

import android.database.Cursor
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.utfpr.usandosqlite.adapter.MeuAdapter
import br.edu.utfpr.usandosqlite.database.DatabaseHandler
import br.edu.utfpr.usandosqlite.databinding.ActivityListarBinding

class ListarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListarBinding
    private lateinit var banco: DatabaseHandler
    private var adapter: MeuAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Temporarily disabled edge-to-edge for diagnostics
        // enableEdgeToEdge()

        binding = ActivityListarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banco = DatabaseHandler.getInstance(this)

        // Temporarily disabled window insets listener for diagnostics
        // ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
        //     val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        //     v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        //     insets
        // }

        // Set the empty view on the ListView
        binding.lvRegistros.emptyView = binding.tvEmptyList

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
}
