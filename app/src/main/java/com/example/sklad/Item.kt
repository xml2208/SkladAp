package com.example.sklad

import com.firebase.ui.firestore.SnapshotParser

data class Item(val title: String, val quantity: Long) {

    fun asMap(): Map<String, Any> = mapOf(
        "title" to title,
        "quantity" to quantity,
    )

    companion object {
        val snapshotParser = SnapshotParser<Item> { snapshot ->
            Item(
                title = snapshot["title"] as String,
                quantity = snapshot["quantity"] as Long,
            )
        }
    }

}
