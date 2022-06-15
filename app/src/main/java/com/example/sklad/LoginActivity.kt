package com.example.sklad

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.content.Intent
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.example.sklad.databinding.ActivityLoginBinding
import com.google.firebase.firestore.Source
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    private lateinit var vb: ActivityLoginBinding
    private val skladRepo = SkladRepository.get()
    private val usersCollection = skladRepo.usersCollection
    private val shp by lazy {
        getSharedPreferences("LoginFile", Context.MODE_PRIVATE)
    }
    private lateinit var name: EditText
    private lateinit var password: EditText
    private val prefManager = PrefManager(this)

    override fun onStart() {
        super.onStart()
        if (shp.contains("name") && shp.contains("password")) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(vb.root)

        name = vb.loginEt.editText!!
        password = vb.passwordEt.editText!!
        vb.continueBtn.setOnClickListener {
            vb.progressBar.visibility = View.VISIBLE
            registerNewUser()
        }
    }

    private fun registerNewUser() = lifecycleScope.launch {
        val name = vb.loginEt.editText?.text.toString()
        val password = vb.passwordEt.editText?.text.toString()
        val user = User(name, password)
        try {
            val userCollection = usersCollection.get(Source.SERVER).await()
            userCollection.documents.forEach {
                if (it["name"] == user.name && it["password"] == user.password) {
                    prefManager.saveLoginDetails(name, password)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    vb.progressBar.visibility = View.GONE
                    return@launch
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this@LoginActivity, "Internetga ulan!!", Toast.LENGTH_LONG).show()
        }
    }
}

