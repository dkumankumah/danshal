package com.example.danshal.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.danshal.R
import com.example.danshal.models.Address
import com.example.danshal.models.Content
import com.example.danshal.models.Event
import com.example.danshal.models.Notification
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.*

class EventRepository() {
    private val db = Firebase.firestore
    private val eventRef = db.collection("events")

    private val _events: MutableLiveData<List<Event>> = MutableLiveData()

    val events: MutableLiveData<List<Event>>
        get() = _events

    // Fetch events from the database for the admin
    suspend fun getAllEvents() {
        try {
            val tempList = arrayListOf<Event>()

            val data = eventRef
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .await()

            for (result in data.toObjects(Event::class.java)) {
                val event = Event(
                    Address(
                        result.address.housenumber,
                        result.address.housenumberExtension.toString(),
                        result.address.postcode,
                        result.address.street,
                        result.address.place
                    ),
                    result.date, result.exclusive, result.ticket
                )
                event.id = result.id
                event.postType = Content.TYPE.EVENT
                event.title = result.title
                event.content = result.content
                event.imageUrl = result.imageUrl
                event.timestamp = result.timestamp

                tempList.add(event)
            }
            _events.value = tempList
        } catch (e: Exception) {
            throw EventRetrievalError("Volgende ging mis: ${e}")
        }
    }

    fun removeEvent(doc: String) {
        try {
            if (doc.isNotEmpty()) {
                eventRef.document(doc).delete()
            } else {
                throw EventRetrievalError("Id is niet gevonden")
            }
        } catch (e: Exception) {
            throw EventRetrievalError("Volgende ging mis: ${e}")
        }
    }

    fun updateEvent(event: Event) {
        try {
            eventRef
                .document(event.id)
                .update(
                    mapOf(
                        "address" to event.address,
                        "content" to event.content,
                        "date" to event.date,
                        "exclusive" to event.exclusive,
                        "imageUrl" to event.imageUrl,
                        "ticket" to event.ticket,
                        "title" to event.title,
                        "timestamp" to FieldValue.serverTimestamp()
                    )
                )
        } catch (e: Exception) {
            throw EventRetrievalError("Volgende ging mis: ${e}")
        }
    }

    // Fetch events from the database for the users
    suspend fun getAllEventsForUsers() {
        _events.value = emptyList()
        try {
            val tempList = arrayListOf<Event>()

            val data = eventRef
                .orderBy("date", Query.Direction.ASCENDING)
                .whereGreaterThanOrEqualTo("date", Timestamp.now().toDate())
                .get()
                .await()

            for (result in data.toObjects(Event::class.java)) {
                val event = Event(
                    Address(
                        result.address.housenumber,
                        result.address.housenumberExtension.toString(),
                        result.address.postcode,
                        result.address.street,
                        result.address.place
                    ),
                    result.date, result.exclusive, result.ticket
                )

                event.postType = Content.TYPE.EVENT
                event.title = result.title
                event.content = result.content
                event.imageUrl = result.imageUrl
                event.timestamp = result.timestamp

                tempList.add(event)
            }
            _events.value = tempList
        } catch (e: Exception) {
            throw EventRetrievalError("Volgende ging mis: ${e}")
        }
    }

    // Only fetch the upcoming events (between today and one week from now)
    suspend fun getUpcomingEvents() {
        _events.value = emptyList()
        try {
            val tempList = arrayListOf<Event>()
            // get today's date and the date of 7 days from now
            val range: Calendar = Calendar.getInstance()
            range.add(Calendar.DATE, +7)
            val end = Timestamp(range.time)

            val data = eventRef
                .orderBy("date", Query.Direction.ASCENDING)
                .whereGreaterThanOrEqualTo("date", Timestamp.now().toDate())
                .whereLessThanOrEqualTo("date", end.toDate())
                .get()
                .await()

            for (result in data.toObjects(Event::class.java)) {
                val event = Event(
                    Address(
                        result.address.housenumber,
                        result.address.housenumberExtension.toString(),
                        result.address.postcode,
                        result.address.street,
                        result.address.place
                    ),
                    result.date, result.exclusive, result.ticket
                )

                event.postType = Content.TYPE.EVENT
                event.title = result.title
                event.content = result.content
                event.imageUrl = result.imageUrl
                event.timestamp = result.timestamp

                tempList.add(event)
            }
            _events.value = tempList
        } catch (e: Exception) {
            throw EventRetrievalError("Volgende ging mis: ${e}")
        }
    }

    class EventRetrievalError(message: String) : Exception(message)
}