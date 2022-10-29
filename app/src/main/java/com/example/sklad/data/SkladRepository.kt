package com.example.sklad.data

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import com.example.sklad.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class SkladRepository(private val context: Context) {
    val skladCollection = FirebaseFirestore.getInstance().collection("Sklad")
    val usersCollection = FirebaseFirestore.getInstance().collection("Users")
    val auth = FirebaseAuth.getInstance()
    private val prefManager = PrefManager(context)

    suspend fun registerNewUser(name: String, password: String): Boolean {
        val user = User(name, password)
        try {
            val userCollection = usersCollection.get(Source.SERVER).await()
            userCollection.documents.forEach {
                if (it["name"] == user.name && it["password"] == user.password) {
                    prefManager.saveLoginDetails(name, password)
                    return true
                } else {
                    Toast.makeText(context, "login yoki parolda xatolik bor!!!", Toast.LENGTH_SHORT).show()
                }
            }
            return false
        } catch (e: Exception) {
            Toast.makeText(context, "Internetga Ulan!!!", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: SkladRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = SkladRepository(context)
            }
        }
        fun get(): SkladRepository {
            return INSTANCE
                ?: throw IllegalStateException("SkladRepository must be initialized!")
        }
    }
}
