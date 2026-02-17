package br.edu.utfpr.usandosqlite
import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import br.edu.utfpr.usandosqlite.adapter.MeuAdapter
import br.edu.utfpr.usandosqlite.database.DatabaseHandler
import br.edu.utfpr.usandosqlite.databinding.ActivityListarBinding
import kotlinx.coroutines.launch

class ListarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListarBinding
    private lateinit var banco: DatabaseHandler
    private lateinit var adapter: MeuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityListarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banco = DatabaseHandler.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btIncluir.setOnClickListener {
            btIncluirOnClick()
        }

        binding.svFiltro.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Não precisamos de ação no submit, o filtro é em tempo real
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Chama a inicialização da lista com o novo texto do filtro
                initList(newText ?: "")
                return true
            }
        })
    }

    override fun onStart() {
        super.onStart()
        // Carrega a lista completa ao iniciar ou retornar para a tela
        initList()
    }

    private fun btIncluirOnClick() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun initList(filtro: String = "") {
        lifecycleScope.launch {
            val cadastros = banco.listar(filtro)
            // O adapter será atualizado para receber uma List<Cadastro>
            adapter = MeuAdapter(this@ListarActivity, cadastros)
            binding.lvRegistros.adapter = adapter
        }
    }

}