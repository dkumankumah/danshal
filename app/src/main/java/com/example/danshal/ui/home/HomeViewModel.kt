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

    val eventListData: LiveData<List<Event>> = eventRepository.events
    val postListData: LiveData<List<Post>> = postRepository.posts


    fun getAllEvents() {
        viewModelScope.launch {
            try {
                eventRepository.getAllEvents()
            } catch (ex: EventRepository.EventRetrievalError) {
                val errorMsg = "Something went wrong while retrieving the events."
                Log.e("HomeViewModel", ex.message ?: errorMsg)
            }
        }
    }

    fun getUpcomingEvents() {
        viewModelScope.launch {
            try {
                eventRepository.getUpcomingEvents()
            } catch (ex: EventRepository.EventRetrievalError) {
                val errorMsg = "Something went wrong while retrieving the upcoming events."
                Log.e("HomeViewModel", ex.message ?: errorMsg)
            }
        }
    }

    fun getAllPosts() {
        viewModelScope.launch {
            try {
                postRepository.getAllPosts()
            } catch (ex: PostRepository.PostRetrievalError) {
                val errorMsg = "Something went wrong while retrieving all posts"
                Log.e("HomeViewModel", ex.message ?: errorMsg)
            }
        }
    }

    fun getUpcomingPosts() {
        viewModelScope.launch {
            try {
                postRepository.getUpcomingPosts()
            } catch (ex: PostRepository.PostRetrievalError) {
                val errorMsg = "Something went wrong while retrieving upcoming posts"
                Log.e("HomeViewModel", ex.message ?: errorMsg)
            }
        }
    }

}