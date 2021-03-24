package com.example.danshal.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.danshal.models.GiveAway
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class GiveAwayRepository {
    private val db = Firebase.firestore
    private val giveawayRef = db.collection("giveaways")

    private val _giveaways: MutableLiveData<List<GiveAway>> = MutableLiveData()

    val giveaways: LiveData<List<GiveAway>>
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

    class GiveAwayRetrievalError(message: String): Exception(message)
}