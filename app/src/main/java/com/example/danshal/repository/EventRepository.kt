package com.example.danshal.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.danshal.models.Address
import com.example.danshal.models.Event
import com.example.danshal.models.EventTest
import com.example.danshal.models.PostEvent
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

    private val _events: MutableLiveData<List<EventTest>> = MutableLiveData()

    val events: LiveData<List<EventTest>>
        get() = _events

    // Fetch events from the database where the date is greater than today's date
    suspend fun getAllEvents() {
        try {
            val tempList = arrayListOf<EventTest>()

            val data = eventRef
                .orderBy("date", Query.Direction.ASCENDING)
                .whereGreaterThanOrEqualTo("date", Timestamp.now().toDate())
                .get()
                .await()

            for (result in data.toObjects(EventTest::class.java)) {
                Log.d("EventRepository", result.title)
                val event = EventTest(
                    Address(
                        result.address.housenumber,
                        result.address.housenumberExtension.toString(),
                        result.address.postcode,
                        result.address.street,
                        result.address.place
                    ),
                    result.date, result.exclusive)

                event.postType = PostEvent.TYPE.EVENT
                event.title = result.title
                event.content = result.content
                event.imageUrl = result.imageUrl

                tempList.add(event)
            }
            _events.value = tempList
        } catch (e: Exception) {
            throw EventRetrievalError("Volgende ging mis: ${e}")
        }
    }

    // Only fetch the upcoming events (between today and one week from now)
    suspend fun getUpcomingEvents() {
        try {
            val tempList = arrayListOf<EventTest>()
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

            for (result in data.toObjects(EventTest::class.java)) {
                val event = EventTest(
                    Address(
                        result.address.housenumber,
                        result.address.housenumberExtension.toString(),
                        result.address.postcode,
                        result.address.street,
                        result.address.place
                    ),
                    result.date, result.exclusive)

                event.postType = PostEvent.TYPE.EVENT
                event.title = result.title
                event.content = result.content
                event.imageUrl = result.imageUrl

                tempList.add(event)
            }
            _events.value = tempList
        } catch (e: Exception) {
            throw EventRetrievalError("Volgende ging mis: ${e}")
        }
    }

    class EventRetrievalError(message: String): Exception(message)
}