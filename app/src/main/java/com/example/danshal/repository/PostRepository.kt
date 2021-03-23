package com.example.danshal.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.danshal.models.Post
import com.example.danshal.models.PostEvent
import com.example.danshal.models.PostTest
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.*

class PostRepository() {
    private val db = Firebase.firestore
    private val postRef = db.collection("posts")

    private val _posts: MutableLiveData<List<PostTest>> = MutableLiveData()

    val posts: LiveData<List<PostTest>>
        get() = _posts

    // Fetch posts from the database where the date is greater than today's date
    suspend fun getAllPosts() {
        try {
            val tempList = arrayListOf<PostTest>()

            val data = postRef
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .whereGreaterThanOrEqualTo("timestamp", Timestamp.now().toDate())
                .get()
                .await()

            for (result in data.toObjects(PostTest::class.java)) {
                val post = PostTest(result.exclusive)
                post.postType = PostEvent.TYPE.POST
                post.title = result.title
                post.content = result.content
                post.imageUrl = result.imageUrl

                tempList.add(post)
            }

            _posts.value = tempList
        } catch (e: Exception) {
            throw PostRetrievalError("Het volgende ging mis: $e")
        }
    }

    // Only fetch the upcoming posts (between today and one week from now)
    suspend fun getUpcomingPosts() {
        try {
            val tempList = arrayListOf<PostTest>()
            // get today's date and the date of 7 days from now
            val range: Calendar = Calendar.getInstance()
            range.add(Calendar.DATE, +7)
            val end = Timestamp(range.time)

            val data = postRef
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .whereGreaterThanOrEqualTo("timestamp", Timestamp.now().toDate())
                .whereLessThanOrEqualTo("timestmap", end.toDate())
                .get()
                .await()

            for (result in data.toObjects(PostTest::class.java)) {
                val post = PostTest(result.exclusive)
                post.postType = PostEvent.TYPE.POST
                post.title = result.title
                post.content = result.content
                post.imageUrl = result.imageUrl

                tempList.add(post)
            }

            _posts.value = tempList
        } catch (e: Exception) {
            throw PostRetrievalError("Het volgende ging mis: $e")
        }
    }

    class PostRetrievalError(message: String): Exception(message)
}