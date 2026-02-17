package br.edu.utfpr.usandosqlite

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import br.edu.utfpr.usandosqlite.database.DatabaseHandler
import br.edu.utfpr.usandosqlite.databinding.ActivityMainBinding
import br.edu.utfpr.usandosqlite.entity.Cadastro
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var banco: DatabaseHandler

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banco = DatabaseHandler.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()
    }

    override fun onStart() {
        super.onStart()
        initView()
    }

    private fun initView() {
        if ( intent.getStringExtra( "cod" ) != null ) {
            binding.etCod.setText( intent.getStringExtra( "cod" ) )
            binding.etNome.setText( intent.getStringExtra( "nome" ) )
            binding.etTelefone.setText( intent.getStringExtra( "telefone" ) )
        } else {
            binding.btExcluir.visibility = View.GONE
            binding.btPesquisar.visibility = View.GONE
        }
    }


    fun btSalvarOnClick(view: View) {
        lifecycleScope.launch {
            var msg = ""

            if (binding.etCod.text.toString().isNullOrEmpty()) {
                val cadastro = Cadastro(
                    "0",
                    binding.etNome.text.toString(),
                    binding.etTelefone.text.toString()
                )
                //acesso ao banco
                banco.inserir(cadastro)
                msg = "Inclusão efetuada com Sucesso."
            } else {
                val cadastro = Cadastro(
                    binding.etCod.text.toString(),
                    binding.etNome.text.toString(),
                    binding.etTelefone.text.toString()
                )
                banco.alterar(cadastro)
                val msg = "Alteração efetuada com Sucesso."
            }

            //apresentação da devolutiva visual para o usuário
            Toast.makeText(
                this@MainActivity,
                msg,
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }

    fun btExcluirOnClick(view: View) {

        //validação dos campos de tela

        //acesso ao banco
        lifecycleScope.launch {
            banco.excluir( binding.etCod.text.toString() )

            //apresentação da devolutiva visual para o usuário
            Toast.makeText(
                this@MainActivity,
                "Exclusão efetuada com Sucesso.",
                Toast.LENGTH_SHORT
            ).show()
        }
        finish()
    }

    fun btPesquisarOnClick(view: View) {

        //validação dos campos de tela

        //acesso ao banco
        val etCodPesquisar = EditText(this)

        val builder = AlertDialog.Builder( this )
        builder.setTitle("Digite o Código")
        builder.setView(etCodPesquisar)
        builder.setCancelable(false)
        builder.setNegativeButton(
            "Fechar",
            null
        )

        builder.setPositiveButton(
            "Pesquisar",
            { dialog, which ->

                lifecycleScope.launch {

                    val cadastro = banco.pesquisar(etCodPesquisar.text.toString())

                    if (cadastro != null) {
                        binding.etCod.setText(etCodPesquisar.text.toString())
                        binding.etNome.setText(cadastro.nome)
                        binding.etTelefone.setText(cadastro.telefone)
                    } else {
                        binding.etNome.setText("")
                        binding.etTelefone.setText("")

                        Toast.makeText(
                            this@MainActivity,
                            "Registro não encontro.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        )

        builder.show()

    }


} //fim da MainActivity