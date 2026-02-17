package br.edu.utfpr.usandosqlite.database

import br.edu.utfpr.usandosqlite.entity.Cadastro
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class DatabaseHandler ()  {

    private val firestore = Firebase.firestore

    companion object {
        private const val COLLECTION_NAME = "cadastro"

        @Volatile
        private var INSTANCE: DatabaseHandler? = null

        fun getInstance(): DatabaseHandler {

            if ( INSTANCE == null ) {
                INSTANCE = DatabaseHandler()
            }
            return INSTANCE as DatabaseHandler
        }

    }

    suspend fun inserir(cadastro: Cadastro) {
        firestore
            .collection(COLLECTION_NAME)
            .add(cadastro)
            .await()
    }

    suspend fun alterar(cadastro: Cadastro) {
        cadastro._id?.let { id ->
            firestore
                .collection(COLLECTION_NAME)
                .document(id )
                .set(cadastro)
                .await()
        }
    }

    suspend fun excluir(id: String) {
        firestore
            .collection(COLLECTION_NAME)
            .document(id)
            .delete()
            .await()
    }

    suspend fun pesquisar(id: String): Cadastro? {
        val document = firestore
            .collection(COLLECTION_NAME)
            .document(id)
            .get()
            .await()
        return document.toObject(Cadastro::class.java)
    }


    suspend fun listar(): List<Cadastro> {
        return listar("")
    }

    suspend fun listar(filtro: String = ""): List<Cadastro> {
        val query = firestore
            .collection(COLLECTION_NAME)

        val snapshot = query
            .get()
            .await()

        val cadastros = snapshot.toObjects<Cadastro>()

        return if (filtro.isNotEmpty()) {
            cadastros.filter { it.nome.contains(filtro, ignoreCase = true) }
        } else {
            cadastros
        }
    }

}