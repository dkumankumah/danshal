package com.example.danshal.repository

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
import java.util.*

class EventRepository() {
    private val db = Firebase.firestore
    private val eventRef = db.collection("events")

    private val _events: MutableLiveData<List<Event>> = MutableLiveData()

    val events: LiveData<List<Event>>
        get() = _events

    // Fetch events from the database where the date is greater than today's date
    suspend fun getAllEvents() {
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
            throw EventRetrievalError("Volgende ging mis: ${e}")
        }
    }

    // Only fetch the upcoming events (between today and one week from now)
    suspend fun getUpcomingEvents() {
        try {
            val tempList = arrayListOf<Event>()
            // get today's date and the date of 7 days from now
            val start: Timestamp = Timestamp.now()
            val range: Calendar = Calendar.getInstance()
            range.add(Calendar.DATE, +7)
            val end = Timestamp(range.time)

            val data = eventRef
                .orderBy("date", Query.Direction.ASCENDING)
                .whereGreaterThanOrEqualTo("date", start.toDate())
                .whereLessThanOrEqualTo("date", end.toDate())
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
            throw EventRetrievalError("Volgende ging mis: ${e}")
        }
    }

    class EventRetrievalError(message: String): Exception(message)
}