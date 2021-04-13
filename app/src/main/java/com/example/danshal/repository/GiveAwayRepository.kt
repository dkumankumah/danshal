package com.example.danshal.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.danshal.R
import com.example.danshal.models.GiveAway
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldPath
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

    private val _giveAwayStatus: MutableLiveData<Boolean> = MutableLiveData()
    val giveAwayStatus: LiveData<Boolean>
        get() = _giveAwayStatus

    // Fetch events from the database where the date is greater than today's date
    suspend fun getAllGiveAways() {
        try {
            val tempList = arrayListOf<GiveAway>()

            val data = giveawayRef
                .orderBy("endDate", Query.Direction.ASCENDING)
                .get()
                .await()

            for (result in data.toObjects(GiveAway::class.java)) {
                val giveAway = GiveAway(result.participants, result.endDate)
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

            val data = giveawayRef
                .orderBy("endDate", Query.Direction.ASCENDING)
                .whereGreaterThanOrEqualTo("endDate", Timestamp.now().toDate())
                .get()
                .await()

            for (result in data.toObjects(GiveAway::class.java)) {
                val tempUserList = arrayListOf<String>()
                // Add user if exists to list
                if (result.participants.isNotEmpty()) {
                    for (user in result.participants) {
                        tempUserList.add(user)
                    }
                }

                val giveAway = GiveAway(tempUserList, result.endDate)
                giveAway.title = result.title
                giveAway.content = result.content
                giveAway.imageUrl = result.imageUrl
                giveAway.timestamp = result.timestamp
                giveAway.id = result.id
                tempList.add(giveAway)
            }
            _giveaways.value = tempList
        } catch (e: Exception) {
            throw GiveAwayRetrievalError("Volgende ging mis: ${e}")
        }
    }

    fun addUserToGiveAway(userId: String, giveAwayId: String) {
        try {
            // The if statement might be redundant. If the user has already subscribed
            // to this giveaway, this function won't be called. So the else statement kind of never
            // happens, EXCEPT if a user has also logged in on an other device and tries to do the same at the same time...
            giveawayRef.whereEqualTo(FieldPath.documentId(), giveAwayId)
                .whereArrayContains("participants", userId)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.size() > 0) {
                        _giveAwayStatus.value = false
                    } else {
                        // If it's their first time, add them to the list
                        giveawayRef.document(giveAwayId)
                            .update("participants", FieldValue.arrayUnion(userId))
                        _giveAwayStatus.value =  true
                    }
                }
        } catch (e: Exception) {
            throw GiveAwayRetrievalError("Volgende ging mis: ${e}")
        }
    }

    fun removeUserFromGiveAway(userId: String, giveAwayId: String) {
        try {
            giveawayRef.document(giveAwayId)
                .update("participants", FieldValue.arrayRemove(userId))
            _giveAwayStatus.value = false
        } catch (e: Exception) {
            throw GiveAwayRetrievalError("Volgende ging mis: ${e}")
        }
    }

    class GiveAwayRetrievalError(message: String) : Exception(message)
}