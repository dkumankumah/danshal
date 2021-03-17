package com.example.danshal.repository

import androidx.lifecycle.LiveData
import com.example.danshal.models.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

interface EventRepository {

    suspend fun getEvents(): LiveData<out List<Event>>
}