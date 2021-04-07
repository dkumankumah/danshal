package com.example.danshal.ui.admin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.danshal.models.Content
import com.example.danshal.models.Event
import com.example.danshal.models.GiveAway
import com.example.danshal.models.Post
import com.example.danshal.repository.EventRepository
import com.example.danshal.repository.GiveAwayRepository
import com.example.danshal.repository.PostRepository
import kotlinx.coroutines.launch

class AdminDashboardViewModel : ViewModel() {
    private val eventRepository = EventRepository()
    private val giveAwayRepository = GiveAwayRepository()
    private val postRepository = PostRepository()

    val eventListData: LiveData<List<Event>> = eventRepository.events
    val giveawayListData: LiveData<List<GiveAway>> = giveAwayRepository.giveaways
    val postListData: LiveData<List<Post>> = postRepository.posts

    lateinit var detailContentType: String

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

    fun getAllGiveAways() {
        viewModelScope.launch {
            try {
                giveAwayRepository.getAllGiveAways()
            } catch (ex: GiveAwayRepository.GiveAwayRetrievalError) {
                val errorMsg = "Something went wrong while retrieving the giveaways."
                Log.e("HomeViewModel", ex.message ?: errorMsg)
            }
        }
    }

    fun getAllPosts() {
        viewModelScope.launch {
            try {
                postRepository.getAllPosts()
            } catch (ex: PostRepository.PostRetrievalError) {
                val errorMsg = "Something went wrong while retrieving the exclusive posts."
                Log.e("HomeViewModel", ex.message ?: errorMsg)
            }
        }
    }

}