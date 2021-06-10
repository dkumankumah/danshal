package com.example.danshal.repository

import android.app.DownloadManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.danshal.models.Address
import com.example.danshal.models.User
import com.google.firebase.firestore.Query
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

    private val _users: MutableLiveData<List<User>> = MutableLiveData()
    val users: LiveData<List<User>>
        get() = _users


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

    suspend fun getAllUsers() {
        try {
            withTimeout(5_000) {
                val tempUserList = arrayListOf<User>()
                val data = userRef
                  .orderBy("naam", Query.Direction.ASCENDING)
                  .get()
                  .await()

                for (result in data.toObjects(User::class.java)) {
                    var user = User(result.naam, result.address, result.email, result.profileImage, result.userId, result.admin)
                    tempUserList.add(user)
                }

                _users.value = tempUserList
                Log.d("UserRepository", "user: " + tempUserList[0])
            }
        } catch (e: Exception) {
            throw PostRepository.PostRetrievalError("Volgende ging mis: ${e}")
        }
    }

    class UserRetrievalError(message: String) : Exception(message)
}