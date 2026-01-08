package br.edu.utfpr.usandosqlite

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.utfpr.usandosqlite.database.DatabaseHandler
import br.edu.utfpr.usandosqlite.databinding.ActivityMainBinding
import br.edu.utfpr.usandosqlite.entity.Cadastro

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var banco: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        banco = DatabaseHandler.getInstance(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    override fun onStart() {
        super.onStart()
        initView()
    }

    private fun initView() {
        if (intent.getIntExtra("cod", 0) != 0) {
            binding.etCod.setText(intent.getIntExtra("cod", 0).toString())
            binding.etNome.setText(intent.getStringExtra("nome"))
            binding.etTelefone.setText(intent.getStringExtra("telefone"))

        } else {
            //tratar a inclusão (novo registro)
            binding.btExcluir.visibility = View.GONE
            binding.btPesquisar.visibility = View.GONE


        }

    }

    fun btSalvarOnClick(view: View) {
        if (binding.etCod.text.toString().isEmpty()) {
            val cadastro = Cadastro(
                _id = 0,
                nome = binding.etNome.text.toString(),
                telefone = binding.etTelefone.text.toString()
            )
            //acesso ao banco
            banco.inserir(cadastro)
            var msg = getString(R.string.inclus_o_realizada_com_sucesso)
        } else {
            val cadastro = Cadastro(
                _id = binding.etCod.text.toString().toInt(),
                nome = binding.etNome.text.toString(),
                telefone = binding.etTelefone.text.toString()
            )
            //acesso ao banco
            banco.alterar(cadastro)
            var msg = getString(R.string.altera_o_realizada_com_sucesso)
        }

        //Mostra uma mensagem de sucesso na tela
        Toast.makeText(
            this,
            "Registro inserido com sucesso",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun btExcluirOnClick(view: View) {

        //Acessa o banco de dados e exclui o registro
        banco.excluir(binding.etCod.text.toString().toInt())

        //Mostra uma mensagem de sucesso na tela
        Toast.makeText(
            this,
            "Registro excluído com sucesso",
            Toast.LENGTH_SHORT
        ).show()

    }

    fun btPesquisarOnClick(view: View) {

        //validação dos campos da tela

        val etCodPesquisar = EditText(this)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Digite o Código")
        builder.setView(etCodPesquisar)
        builder.setCancelable(false)
        builder.setNegativeButton("Fechar", null)
        builder.setPositiveButton("Pesquisar", { dialog, which ->
            val cadastro = banco.pesquisar(etCodPesquisar.text.toString().toInt())

            if (cadastro != null) {
                binding.etCod.setText(cadastro._id.toString())
                binding.etNome.setText(cadastro.nome)
                binding.etTelefone.setText(cadastro.telefone)

            } else {

                binding.etNome.setText("")
                binding.etTelefone.setText("")
                Toast.makeText(
                    this,
                    "Registro não encontrado",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
        )


        builder.show()


        fun btListarOnClick(view: View) {
            val intent = Intent(this, ListarActivity::class.java)
            startActivity(intent)
        }


        //Acessa o banco de dados e lista os registros
        // val registros = banco.listar()

        //Monta a saída para o usuário
        //val saida = StringBuilder()

        //while (registros.moveToNext()) {
        //   val nome = registros.getString(DatabaseHandler.COL_NOME.toInt())
        //  val telefone  = registros.getString(DatabaseHandler.COL_TELEFONE.toInt())
        //
        //   saida.append("${nome} - ${telefone} \n")
        // }
        // Toast.makeText(
        //    this,
        //   saida.toString(),
        //    Toast.LENGTH_SHORT)
        //   .show()


    } //Fim da classe MainActivity

}