package com.example.danshal.ui.admin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.danshal.models.Event
import com.example.danshal.models.GiveAway
import com.example.danshal.models.Post
import com.example.danshal.models.User
import com.example.danshal.repository.EventRepository
import com.example.danshal.repository.GiveAwayRepository
import com.example.danshal.repository.PostRepository
import com.example.danshal.repository.UserRepository
import kotlinx.coroutines.launch

class AdminDashboardViewModel : ViewModel() {
    private val eventRepository = EventRepository()
    private val giveAwayRepository = GiveAwayRepository()
    private val postRepository = PostRepository()
    private val userRepository = UserRepository()

    val eventListData: LiveData<List<Event>> = eventRepository.events
    val giveawayListData: LiveData<List<GiveAway>> = giveAwayRepository.giveaways
    val postListData: LiveData<List<Post>> = postRepository.posts
    val giveawayWinnerData: LiveData<User> = userRepository.giveawayWinner

    lateinit var detailContentType: String

    private val _currentGiveAway: MutableLiveData<GiveAway> = MutableLiveData()
    val currentGiveAway: LiveData<GiveAway>
        get() = _currentGiveAway

    fun getAllEvents() {
        viewModelScope.launch {
            try {
                eventRepository.getAllEvents()
            } catch (ex: EventRepository.EventRetrievalError) {
                val errorMsg = "Something went wrong while retrieving the events."
                Log.e("ADMIN_DASHBOARD", ex.message ?: errorMsg)
            }
        }
    }

    fun getAllGiveAways() {
        viewModelScope.launch {
            try {
                giveAwayRepository.getAllGiveAways()
            } catch (ex: GiveAwayRepository.GiveAwayRetrievalError) {
                val errorMsg = "Something went wrong while retrieving the giveaways."
                Log.e("ADMIN_DASHBOARD", ex.message ?: errorMsg)
            }
        }
    }

    fun getAllPosts() {
        viewModelScope.launch {
            try {
                postRepository.getAllPosts()
            } catch (ex: PostRepository.PostRetrievalError) {
                val errorMsg = "Something went wrong while retrieving the exclusive posts."
                Log.e("ADMIN_DASHBOARD", ex.message ?: errorMsg)
            }
        }
    }

    fun removeEvent(doc: String) {
        viewModelScope.launch {
            try {
                eventRepository.removeEvent(doc)
            } catch (ex: PostRepository.PostRetrievalError) {
                val errorMsg = "Something went wrong while removing an event."
                Log.e("ADMIN_DASHBOARD", ex.message ?: errorMsg)
            }
        }
    }

    fun removePost(doc: String) {
        viewModelScope.launch {
            try {
                postRepository.removePost(doc)
            } catch (ex: PostRepository.PostRetrievalError) {
                val errorMsg = "Something went wrong while removing a post."
                Log.e("ADMIN_DASHBOARD", ex.message ?: errorMsg)
            }
        }
    }

    fun removeGiveaway(doc: String) {
        viewModelScope.launch {
            try {
                giveAwayRepository.removeGiveaway(doc)
            } catch (ex: PostRepository.PostRetrievalError) {
                val errorMsg = "Something went wrong while removing a giveaway."
                Log.e("ADMIN_DASHBOARD", ex.message ?: errorMsg)
            }
        }
    }

    fun userById(id: String) {
        viewModelScope.launch {
            try {
                userRepository.userById(id)
            } catch (ex: UserRepository.UserRetrievalError) {
                val errorMsg = "Something went wrong while retrieving giveaway winner."
                Log.e("ADMIN_DASHBOARD", ex.message ?: errorMsg)
            }
        }
    }

    fun setCurrentGiveAway(giveAway: GiveAway) {
        _currentGiveAway.value = giveAway
    }

}