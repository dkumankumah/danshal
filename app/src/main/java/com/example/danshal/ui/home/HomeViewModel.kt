package com.example.danshal.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.danshal.R
import com.example.danshal.models.*
import com.example.danshal.repository.EventRepository
import com.example.danshal.repository.GiveAwayRepository
import com.example.danshal.repository.PostRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val eventRepository = EventRepository()
    private val postRepository = PostRepository()
    private val giveAwayRepository = GiveAwayRepository()
    private var auth: FirebaseAuth

    // All the lists for the data
    private val eventListData: MutableLiveData<List<Event>> = eventRepository.events
    private val postListData: MutableLiveData<List<Post>> = postRepository.posts
    private val giveAwayListData: MutableLiveData<List<GiveAway>> = giveAwayRepository.giveaways
    private val contentListData: MediatorLiveData<List<Content>> = MediatorLiveData()

    // Checks for functions
    private val _userLoggedIn: MutableLiveData<Boolean> = MutableLiveData()
    private val giveAwaySucceeded: MutableLiveData<Boolean> = giveAwayRepository.giveAwayStatus

    var currentGiveAway: MutableLiveData<GiveAway> = MutableLiveData<GiveAway>()
    val currentContentType: MutableLiveData<String> = MutableLiveData<String>()

    val userLoggedIn: MutableLiveData<Boolean>
        get() = _userLoggedIn


    init {
        currentContentType.value = R.string.title_content.toString()
        auth = Firebase.auth
        combineAllContent()
        _userLoggedIn.value = Firebase.auth.currentUser != null
    }

    // Voor nu zijn de filters hardcoded, moet er later uit
    // user auth moet hierover heen gedaan worden
    fun loadAllContent() {
        viewModelScope.launch {
            try {
                when (currentContentType.value.toString()) {
                    "Upcoming Events" -> {
                        eventRepository.getUpcomingEvents()
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

    //     Merge the two liveData by adding it to the sources of MediatorLiveData
    private fun combineAllContent() {
        viewModelScope.launch {
            contentListData.value = emptyList()

            contentListData.removeSource(eventListData)
            contentListData.addSource(eventListData) { value ->
                contentListData.value = value
            }

            contentListData.removeSource(postListData)
            contentListData.addSource(postListData) { value ->
                contentListData.value = value
            }
        }
    }

    fun addUserToGiveAway(giveAwayId: String) {
        if (auth.currentUser != null) {
            viewModelScope.launch {
                try {
                    giveAwayRepository.addUserToGiveAway(auth.currentUser!!.uid, giveAwayId)
                }
                catch (ex: GiveAwayRepository.GiveAwayRetrievalError) {
                    val errorMsg = "Something went wrong while adding user to a giveaway."
                    Log.e("HomeViewModel", ex.message ?: errorMsg)
                }
            }
            getGiveAway()
        } else {
            println("niet ingelogd")
        }
    }

    fun removeUserFromGiveAway(giveAwayId: String) {
        if (auth.currentUser != null) {
            viewModelScope.launch {
                try {
                    giveAwayRepository.removeUserFromGiveAway(auth.currentUser!!.uid, giveAwayId)
                }
                catch (ex: GiveAwayRepository.GiveAwayRetrievalError) {
                    val errorMsg = "Something went wrong while adding user to a giveaway."
                    Log.e("HomeViewModel", ex.message ?: errorMsg)
                }
            }
            getGiveAway()
        } else {
            println("niet ingelogd")
        }
    }

    // observe this function to let the user know if they successfully entered the giveaway
    // Might be a better way to do this though
    fun getGiveAwayStatus(): MutableLiveData<Boolean> {
        return giveAwaySucceeded
    }

    fun getContent(): MediatorLiveData<List<Content>> {
        return contentListData
    }

    fun getGiveAway(): MutableLiveData<List<GiveAway>> {
        return giveAwayListData
    }
}

