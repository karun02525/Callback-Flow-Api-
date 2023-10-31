package com.example.callbackflowapi


import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

fun Context.networkAvailabilityFlow() = callbackFlow {
    val callback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            trySend(true)
        }

        override fun onLost(network: Network) {
            trySend(false)
        }

        override fun onUnavailable() {
            trySend(false)
        }
    }

    val manager =
        this@networkAvailabilityFlow.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    manager.registerNetworkCallback(
        NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build(), callback
    )
    awaitClose {
        manager.unregisterNetworkCallback(callback)
    }
}


fun EditText.changeText() = callbackFlow {

    val callback = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            trySend(p0)
        }
    }

    addTextChangedListener(callback)

    awaitClose { removeTextChangedListener(callback) }


}