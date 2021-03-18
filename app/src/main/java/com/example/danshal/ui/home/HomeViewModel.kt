package com.example.danshal.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.danshal.models.Event
import com.example.danshal.repository.EventRepository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application){
    private val eventRepository = EventRepository()

    val eventListData: LiveData<List<Event>> = eventRepository.events


    fun getEvents() {
        viewModelScope.launch {
            try {
                eventRepository.getEvents()
            } catch (ex: EventRepository.EventRetrievalError) {
                val errorMsg = "Something went wrong while retrieving the events."
                Log.e("HomeViewModel", ex.message ?: errorMsg)
            }
        }
    }



}