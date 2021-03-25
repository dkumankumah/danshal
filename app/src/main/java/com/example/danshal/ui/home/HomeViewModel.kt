package com.example.danshal.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.danshal.R
import com.example.danshal.models.Event
import com.example.danshal.models.GiveAway
import com.example.danshal.models.Post
import com.example.danshal.repository.EventRepository
import com.example.danshal.repository.GiveAwayRepository
import com.example.danshal.repository.PostRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val eventRepository = EventRepository()
    private val postRepository = PostRepository()
    private val giveAwayRepository = GiveAwayRepository()

    private val eventListData: MutableLiveData<List<Event>> = eventRepository.events
    private val postListData: MutableLiveData<List<Post>> = postRepository.posts
    private val giveAwayListData: MutableLiveData<List<GiveAway>> = giveAwayRepository.giveaways

    val currentEvent: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    // Voor nu zijn de filters hardcoded, moet er later uit
    // user auth moet hierover heen gedaan worden
    fun loadAllContent() {
        viewModelScope.launch {
            try {
                when (currentEvent.value.toString()) {
                    "Upcoming Events" -> {
                        eventRepository.getUpcomingEvents()
                    }
                    "Events" -> {
                        eventRepository.getAllEventsForUsers()
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
                val errorMsg = "Something went wrong while retrieving the content."
                Log.e("HomeViewModel", ex.message ?: errorMsg)
            }
        }
    }

    fun loadGiveAway() {
        viewModelScope.launch {
            try {
                giveAwayRepository.getAllGiveAwaysForUsers()
            } catch (ex: GiveAwayRepository.GiveAwayRetrievalError) {
                val errorMsg = "Something went wrong while retrieving the giveaway."
                Log.e("HomeViewModel", ex.message ?: errorMsg)
            }
        }
    }

    fun getEvents(): MutableLiveData<List<Event>> {
        return eventListData
    }

    fun getPosts(): MutableLiveData<List<Post>> {
        return postListData
    }

    fun getGiveAway(): MutableLiveData<List<GiveAway>> {
        Log.d("ViewModel", giveAwayListData.value?.size.toString())
        return giveAwayListData
    }
}