package com.example.sklad

import com.firebase.ui.firestore.SnapshotParser

data class User(var name: String, var password: String) {
    fun asMap(): Map<String, String> =
        mapOf(
            "name" to name,
            "password" to password
        )

    companion object {
        val snapshotParser = SnapshotParser<User> { snapshot ->
            User(
                name = snapshot["name"] as String,
                password = snapshot["password"] as String
            )
        }
    }
}