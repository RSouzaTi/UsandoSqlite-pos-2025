package br.edu.utfpr.usandosqlite

import android.content.Intent
import android.os.Bundle
import android.view.View
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


        //Validação dos campos da tela
    fun btIncluirOnClick(view: View) {
        val cadastro = Cadastro(binding.etCod.text.toString().toInt(),
            binding.etNome.text.toString(),
            binding.etTelefone.text.toString()
        )
            //Acessa o banco de dados e insere o registro
            banco.inserir(cadastro)

        //Apresentação da devolutiva visual para o usuário
        Toast.makeText(
            this,
            "Registro inserido com sucesso",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun btAlterarOnClick(view: View) {
        //Cria um objeto do tipo Cadastro com os dados da tela
        val cadastro = Cadastro( binding.etCod.text.toString().toInt(),
            binding.etNome.text.toString(),
            binding.etTelefone.text.toString()
        )
            //Acessa o banco de dados e altera o registro
            banco.alterar(cadastro)

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


        //Acesso ao banco de dados e pesquisa o registro
        val cadastro = banco.pesquisar(binding.etCod.text.toString().toInt())

        if (cadastro != null) {

            binding.etNome.setText( cadastro.nome)
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

