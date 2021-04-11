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
import com.google.firebase.auth.FirebaseUser
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
    val giveAwayStatus: LiveData<Boolean> = giveAwayRepository.giveAwayStatus

    var currentGiveAway: MutableLiveData<GiveAway> = MutableLiveData<GiveAway>()
    val currentContentType: MutableLiveData<String> = MutableLiveData<String>()

    private val _userLoggedIn: MutableLiveData<FirebaseUser> = MutableLiveData()
    val userLoggedIn: MutableLiveData<FirebaseUser>
        get() = _userLoggedIn

    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String>
        get() = _errorText


    init {
        currentContentType.value = R.string.title_content.toString()
        auth = Firebase.auth
        combineAllContent()
        _userLoggedIn.value = Firebase.auth.currentUser
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
                _errorText.value = errorMsg
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
                _errorText.value = errorMsg
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
                    loadGiveAway()
                }
                catch (ex: GiveAwayRepository.GiveAwayRetrievalError) {
                    val errorMsg = "Something went wrong while adding user to a giveaway."
                    Log.e("HomeViewModel", ex.message ?: errorMsg)
                    _errorText.value = errorMsg
                }
            }
        } else {
            println("niet ingelogd")
        }
    }

    fun removeUserFromGiveAway(giveAwayId: String) {
        if (auth.currentUser != null) {
            viewModelScope.launch {
                try {
                    giveAwayRepository.removeUserFromGiveAway(auth.currentUser!!.uid, giveAwayId)
                    loadGiveAway()
                }
                catch (ex: GiveAwayRepository.GiveAwayRetrievalError) {
                    val errorMsg = "Something went wrong while adding user to a giveaway."
                    Log.e("HomeViewModel", ex.message ?: errorMsg)
                    _errorText.value = errorMsg
                }
            }
        } else {
            println("niet ingelogd")
        }
    }

    fun getContent(): MediatorLiveData<List<Content>> {
        return contentListData
    }

    fun getGiveAway(): MutableLiveData<List<GiveAway>> {
        return giveAwayListData
    }
}

