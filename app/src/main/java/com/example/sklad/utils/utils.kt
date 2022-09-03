package com.example.sklad

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.cancellation.CancellationException

fun DocumentReference.listenUpdatesFlow(): Flow<DocumentSnapshot> = callbackFlow {
    val listener = this@listenUpdatesFlow.addSnapshotListener { event, exception ->
        if (exception != null) {
            cancel(CancellationException(exception))
        }
        if (event != null) {
            trySend(event)
        }
    }

    awaitClose {
        Log.d("SkladApp", "listenUpdatesFlow: closing ${this@listenUpdatesFlow.id}")
        listener.remove()
    }
}


fun CollectionReference.listenUpdatesFlow(): Flow<QuerySnapshot> = callbackFlow {
    val listener = this@listenUpdatesFlow.addSnapshotListener { event, exception ->
        if (exception != null) {
            cancel(CancellationException(exception))
        }
        if (event != null) {
            trySend(event)
        }
    }
    awaitClose {
        Log.d("SkladApp", "listenUpdatesFlow: closing ${this@listenUpdatesFlow.id}")
        listener.remove()
    }
}

