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

    private val _currentGiveAway: MutableLiveData<GiveAway> = MutableLiveData()
    val currentGiveAway: LiveData<GiveAway>
        get() = _currentGiveAway

    val currentContentType: MutableLiveData<String> = MutableLiveData<String>()

    private val _isSubscribed: MutableLiveData<Boolean> = MutableLiveData()
    val isSubscribed: LiveData<Boolean>
        get() = _isSubscribed

    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String>
        get() = _errorText


    init {
        currentContentType.value = R.string.title_content.toString()
        auth = Firebase.auth
        combineAllContent()
    }

    // Voor nu zijn de filters hardcoded, moet er later uit
    // user auth moet hierover heen gedaan worden
    fun loadAllContent() {
        viewModelScope.launch {
            try {
                if(isLoggedIn()) {
                    when (currentContentType.value.toString()) {
                        "Upcoming Events" -> {
                            eventRepository.getUpcomingEvents()
                        }
                        else -> {
                            eventRepository.getAllEventsForUsers()
                            postRepository.getAllPostsForUsers()
                        }
                    }
                } else {
                    eventRepository.getAllEventsForUsers()
                    postRepository.getAllNonExclusivePostsForUsers()
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

    fun checkUser() {
        if (isLoggedIn()) {
            _isSubscribed.value =
                currentGiveAway.value?.participants?.contains(auth.currentUser!!.uid)
        } else {
            _isSubscribed.value = false
        }
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun addUserToGiveAway() {
        viewModelScope.launch {
            try {
                giveAwayRepository.addUserToGiveAway(
                    auth.currentUser!!.uid,
                    currentGiveAway.value?.id.toString()
                )
                loadGiveAway()
            } catch (ex: GiveAwayRepository.GiveAwayRetrievalError) {
                val errorMsg = "Something went wrong while adding user to a giveaway."
                Log.e("HomeViewModel", ex.message ?: errorMsg)
                _errorText.value = errorMsg
            }
        }

    }

    fun removeUserFromGiveAway() {
        viewModelScope.launch {
            try {
                giveAwayRepository.removeUserFromGiveAway(
                    auth.currentUser!!.uid,
                    currentGiveAway.value?.id.toString()
                )
                loadGiveAway()
            } catch (ex: GiveAwayRepository.GiveAwayRetrievalError) {
                val errorMsg = "Something went wrong while adding user to a giveaway."
                Log.e("HomeViewModel", ex.message ?: errorMsg)
                _errorText.value = errorMsg
            }
        }
    }

    fun setCurrentGiveAway(giveAway: GiveAway) {
        _currentGiveAway.value = giveAway
    }

    fun getContent(): MediatorLiveData<List<Content>> {
        return contentListData
    }

    fun getGiveAway(): MutableLiveData<List<GiveAway>> {
        return giveAwayListData
    }
}

