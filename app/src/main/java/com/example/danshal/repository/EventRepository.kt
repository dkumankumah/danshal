package com.example.danshal.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.danshal.models.Address
import com.example.danshal.models.Event
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class EventRepository() {
    private val db = Firebase.firestore
    private val eventRef = db.collection("events")

    private val _events: MutableLiveData<List<Event>> = MutableLiveData()

    val events: LiveData<List<Event>>
        get() = _events

    // Fetch events from the database where the date is greater than today's date
    suspend fun getEvents() {
        try {
            val tempList = arrayListOf<Event>()

            val data = eventRef
                .orderBy("date", Query.Direction.ASCENDING)
                .whereGreaterThanOrEqualTo("date", Timestamp.now().toDate())
                .get()
                .await()

            for (result in data.toObjects(Event::class.java)) {
                tempList.add(
                    Event(
                        result.title, result.content,
                        Address(
                            result.address.housenumber,
                            result.address.housenumberExtension.toString(),
                            result.address.postcode,
                            result.address.street,
                            result.address.place
                        ),
                        result.date, result.exclusive, result.image
                    )
                )
            }
            _events.value = tempList
        } catch (e: Exception) {
//            throw
            Log.d("EventRepository", "NEEEEEEE")
            throw EventRetrievalError("ging mis dit")
        }

    }

    class EventRetrievalError(message: String): Exception(message)
}