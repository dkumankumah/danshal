package com.example.danshal.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.danshal.models.Event
import com.example.danshal.models.Post
import com.example.danshal.repository.EventRepository
import com.example.danshal.repository.PostRepository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application){
    private val eventRepository = EventRepository()
    private val postRepository = PostRepository()

    val eventListData: MutableLiveData<List<Event>> = eventRepository.events
    val postListData: MutableLiveData<List<Post>> = postRepository.posts


    init {
        loadAllEvents()
        loadAllPosts()
    }

    private fun loadAllEvents() {
        viewModelScope.launch {
            try {
                eventRepository.getAllEventsForUsers()
            } catch (ex: EventRepository.EventRetrievalError) {
                val errorMsg = "Something went wrong while retrieving the events."
                Log.e("HomeViewModel", ex.message ?: errorMsg)
            }
        }
    }

    private fun loadAllPosts() {
        viewModelScope.launch {
            try {
                postRepository.getAllPostsForUsers()
            } catch (ex: PostRepository.PostRetrievalError) {
                val errorMsg = "Something went wrong while retrieving all posts"
                Log.e("HomeViewModel", ex.message ?: errorMsg)
            }
        }
    }

    private fun loadUpcomingEvents() {
        viewModelScope.launch {
            try {
                eventRepository.getUpcomingEvents()
            } catch (ex: EventRepository.EventRetrievalError) {
                val errorMsg = "Something went wrong while retrieving the upcoming events."
                Log.e("HomeViewModel", ex.message ?: errorMsg)
            }
        }
    }

    fun getAllEvents(): MutableLiveData<List<Event>> {
        return eventListData
    }

    fun getAllPosts(): MutableLiveData<List<Post>> {
        return postListData
    }

    fun getUpcomingEvents(): MutableLiveData<List<Event>> {
        return eventListData
    }


}