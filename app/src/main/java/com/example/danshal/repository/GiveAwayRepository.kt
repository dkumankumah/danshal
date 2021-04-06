package com.example.danshal.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.danshal.models.GiveAway
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class GiveAwayRepository {
    private val db = Firebase.firestore
    private val giveawayRef = db.collection("giveaways")

    private val _giveaways: MutableLiveData<List<GiveAway>> = MutableLiveData()

    val giveaways: MutableLiveData<List<GiveAway>>
        get() = _giveaways

    // Fetch events from the database where the date is greater than today's date
    suspend fun getAllGiveAways() {
        try {
            val tempList = arrayListOf<GiveAway>()

            val data = giveawayRef
                .orderBy("endDate", Query.Direction.ASCENDING)
                .get()
                .await()

            for (result in data.toObjects(GiveAway::class.java)) {
                val giveAway = GiveAway( result.participants, result.endDate)
                giveAway.title = result.title
                giveAway.content = result.content
                giveAway.imageUrl = result.imageUrl
                giveAway.timestamp = result.timestamp

                tempList.add(giveAway)
            }
            _giveaways.value = tempList
        } catch (e: Exception) {
            throw GiveAwayRetrievalError("Volgende ging mis: ${e}")
        }
    }

    suspend fun getAllGiveAwaysForUsers() {
        try {
            val tempList = arrayListOf<GiveAway>()
            val tempUserList = arrayListOf<String>()

            val data = giveawayRef
                .orderBy("timestamp", Query.Direction.ASCENDING)
//                .orderBy("endDate", Query.Direction.ASCENDING)
//                .whereGreaterThanOrEqualTo("timestamp", Timestamp.now().toDate())
                .get()
                .await()

            for ((count, result) in data.toObjects(GiveAway::class.java).withIndex()) {
                // Add user if exists to list
                if(result.participants.isNotEmpty()) {
                    for (user in result.participants) {
                        tempUserList.add(user)
                    }
                }

                val giveAway = GiveAway( tempUserList, result.endDate)
                giveAway.title = result.title
                giveAway.content = result.content
                giveAway.imageUrl = result.imageUrl
                giveAway.timestamp = result.timestamp
                giveAway.id = data.documents[count].id

                tempList.add(giveAway)
            }

            _giveaways.value = tempList
        } catch (e: Exception) {
            throw GiveAwayRetrievalError("Volgende ging mis: ${e}")
        }
    }

    fun addUserToGiveAway(userId: String, giveAwayId: String) {
        Log.d("GiveAwayRepository", "ID VAN USER: $userId")
        try {
            giveawayRef.document(giveAwayId).update("participants", FieldValue.arrayUnion(userId))
        } catch (e: Exception) {
            throw GiveAwayRetrievalError("Volgende ging mis: ${e}")
        }
    }

    class GiveAwayRetrievalError(message: String): Exception(message)
}