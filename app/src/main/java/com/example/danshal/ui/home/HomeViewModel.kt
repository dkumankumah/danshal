package com.example.danshal.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.danshal.R
import com.example.danshal.models.Event
import com.example.danshal.models.Post
import com.example.danshal.repository.EventRepository
import com.example.danshal.repository.PostRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val eventRepository = EventRepository()
    private val postRepository = PostRepository()

    private val eventListData: MutableLiveData<List<Event>> = eventRepository.events
    private val postListData: MutableLiveData<List<Post>> = postRepository.posts

    val currentEvent: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    // Voor nu zijn de filters hardcoded, moet er later uit
    fun loadAllContent() {
        viewModelScope.launch {
            try {
                when (currentEvent.value.toString()) {
                    "Upcoming Events" -> {
                        eventRepository.getUpcomingEvents()
                    }
                    "Events" -> {
                        eventRepository.getAllEventsForUsers()
                        postRepository.getAllPostsForUsers()
                    }
                    "Posts" -> {
                        postRepository.getAllPostsForUsers()
                    }
                    else -> {
                        eventRepository.getAllEventsForUsers()
                        postRepository.getAllPostsForUsers()
                    }
                }
            } catch (ex: EventRepository.EventRetrievalError) {
                val errorMsg = "Something went wrong while retrieving the events."
                Log.e("HomeViewModel", ex.message ?: errorMsg)
            }
        }
    }

    private fun getUpcomingEvents() {
        val newList = arrayListOf<Event>()

        val range: Calendar = Calendar.getInstance()
        range.add(Calendar.DATE, +7)
        val end = Timestamp(range.time)

        for (event in eventListData.value!!) {
            if (event.date >= Timestamp.now().toDate() && event.date <= end.toDate()) {
                newList.add(event)
            }
        }

        eventListData.value = newList
    }

    fun getEvents(): MutableLiveData<List<Event>> {
        return eventListData
    }

    fun getPosts(): MutableLiveData<List<Post>> {
        return postListData
    }
}