package com.example.danshal.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.danshal.models.Event

@Dao
interface EventDao {

    @Query("select * from eventTable")
    fun getEvents(): LiveData<List<Event>>
}