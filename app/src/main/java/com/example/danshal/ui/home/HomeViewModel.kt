package com.example.danshal.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.example.danshal.R
import com.example.danshal.models.*
import com.example.danshal.repository.EventRepository
import com.example.danshal.repository.GiveAwayRepository
import com.example.danshal.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*


class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val eventRepository = EventRepository()
    private val postRepository = PostRepository()
    private val giveAwayRepository = GiveAwayRepository()

    private val eventListData: MutableLiveData<List<Event>> = eventRepository.events
    private val postListData: MutableLiveData<List<Post>> = postRepository.posts
    private val giveAwayListData: MutableLiveData<List<GiveAway>> = giveAwayRepository.giveaways

    private val contentListData: MediatorLiveData<List<Content>> = MediatorLiveData()

    val currentEvent: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    init {
        currentEvent.value = R.string.title_content.toString()
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
                    else -> {
                        eventRepository.getAllEventsForUsers()
                        postRepository.getAllPostsForUsers()
                    }
                }
                combineAllContent()
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

//     Merge the two liveData by adding it to MediatorLiveData
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

    fun getContent(): MediatorLiveData<List<Content>> {
        return contentListData
    }

    fun getGiveAway(): MutableLiveData<List<GiveAway>> {
        return giveAwayListData
    }
}

