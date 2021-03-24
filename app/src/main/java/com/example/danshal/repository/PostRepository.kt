package com.example.danshal.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.danshal.models.Content
import com.example.danshal.models.Post
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

    private val _posts: MutableLiveData<List<Post>> = MutableLiveData()

    val posts: LiveData<List<Post>>
        get() = _posts

    // Fetch posts from the database where the date is greater than today's date
    suspend fun getAllPosts() {
        try {
            val tempList = arrayListOf<Post>()

            val data = postRef
                .orderBy("timestamp", Query.Direction.ASCENDING)
//                .whereGreaterThanOrEqualTo("timestamp", Timestamp.now().toDate())
                .get()
                .await()

            for (result in data.toObjects(Post::class.java)) {
                val post = Post(result.exclusive)
                post.postType = Content.TYPE.POST
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
            val tempList = arrayListOf<Post>()
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

            for (result in data.toObjects(Post::class.java)) {
                val post = Post(result.exclusive)
                post.postType = Content.TYPE.POST
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