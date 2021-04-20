package com.example.danshal.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.danshal.models.Address
import com.example.danshal.models.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import java.lang.Exception

class UserRepository {
    private val db = Firebase.firestore
    private val userRef = db.collection("users")

    private val _giveawayWinner: MutableLiveData<User> = MutableLiveData()

    val giveawayWinner: MutableLiveData<User>
        get() = _giveawayWinner

    suspend fun userById(doc: String) {
        try {
            withTimeout(5_000) {
                val data = userRef
                    .document(doc)
                    .get()
                    .await()

                val retrievedUser = data.toObject(User::class.java)
                retrievedUser?.let {
                    giveawayWinner.value = User(
                        retrievedUser.naam,
                        retrievedUser.address?.let { address ->
                            Address(address.housenumber,
                                address.housenumberExtension,
                                address.postcode, address.street,
                                address.place)
                        },
                        retrievedUser.email,
                        retrievedUser.profileImage,
                        retrievedUser.userId,
                        retrievedUser.admin
                    )
                }
            }
        } catch (e: Exception) {
            throw PostRepository.PostRetrievalError("Volgende ging mis: ${e}")
        }
    }

    class UserRetrievalError(message: String) : Exception(message)
}