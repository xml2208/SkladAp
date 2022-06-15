package com.example.sklad

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class SkladRepository constructor(context: Context) {

    val skladCollection =  FirebaseFirestore.getInstance().collection("Sklad")
    val usersCollection = FirebaseFirestore.getInstance().collection("Users")
    val auth = FirebaseAuth.getInstance()

    companion object {
        private var INSTANCE: SkladRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = SkladRepository(context)
            }
        }

        fun get(): SkladRepository {
            return INSTANCE ?:
            throw IllegalStateException("SkladRepository must be initialized!")
        }
    }
}