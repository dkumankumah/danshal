package com.example.danshal.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.danshal.models.Post
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.*

class PostRepository {
    private val db = Firebase.firestore
    private val postRef = db.collection("posts")

    private val _posts: MutableLiveData<List<Post>> = MutableLiveData()
    private val _exclusivePosts: MutableLiveData<List<Post>> = MutableLiveData()
    private val _nonExclusivePosts: MutableLiveData<List<Post>> = MutableLiveData()

    val posts: MutableLiveData<List<Post>>
        get() = _posts

    val exclusivePosts: LiveData<List<Post>>
        get() = _exclusivePosts

    val nonExclusivePosts: LiveData<List<Post>>
        get() = _nonExclusivePosts

    suspend fun getAllPosts() {
        try {
            val tempList = arrayListOf<Post>()

            val data = postRef
                .get()
                .await()

            for (result in data.toObjects(Post::class.java)) {
                val post = Post(result.exclusive,)
                post.title = result.title
                post.content = result.content
                post.imageUrl = result.imageUrl
                post.timestamp = result.timestamp

                tempList.add(post)
            }
            _posts.value = tempList
        } catch (e: Exception) {
            throw PostRetrievalError("Volgende ging mis: ${e}")
        }
    }

    suspend fun getAllExclusivePosts() {
        try {
            val tempList = arrayListOf<Post>()

            val data = postRef
                .whereEqualTo("exclusive", true)
                .get()
                .await()

            for (result in data.toObjects(Post::class.java)) {
                val post = Post(result.exclusive,)
                post.title = result.title
                post.content = result.content
                post.imageUrl = result.imageUrl
                post.timestamp = result.timestamp

                tempList.add(post)
            }
            _exclusivePosts.value = tempList
        } catch (e: Exception) {
            throw PostRetrievalError("Volgende ging mis: ${e}")
        }
    }

    suspend fun getAllNonExclusivePosts() {
        try {
            val tempList = arrayListOf<Post>()

            val data = postRef
                .whereEqualTo("exclusive", false)
                .get()
                .await()

            for (result in data.toObjects(Post::class.java)) {
                val post = Post(result.exclusive,)
                post.title = result.title
                post.content = result.content
                post.imageUrl = result.imageUrl
                post.timestamp = result.timestamp

                tempList.add(post)
            }
            _nonExclusivePosts.value = tempList
        } catch (e: Exception) {
            throw PostRetrievalError("Volgende ging mis: ${e}")
        }
    }

    // The following methods are for the users (fetch posts with date from today)
    suspend fun getAllPostsForUsers() {
        try {
            val tempList = arrayListOf<Post>()

            val data = postRef
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .whereGreaterThanOrEqualTo("timestamp", Timestamp.now().toDate())
                .get()
                .await()

            for (result in data.toObjects(Post::class.java)) {
                val post = Post(result.exclusive,)
                post.title = result.title
                post.content = result.content
                post.imageUrl = result.imageUrl
                post.timestamp = result.timestamp

                tempList.add(post)
            }
            _posts.value = tempList
        } catch (e: Exception) {
            throw PostRetrievalError("Volgende ging mis: ${e}")
        }
    }

    suspend fun getAllExclusivePostsForUsers() {
        try {
            val tempList = arrayListOf<Post>()

            val data = postRef
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .whereGreaterThanOrEqualTo("timestamp", Timestamp.now().toDate())
                .whereEqualTo("exclusive", true)
                .get()
                .await()

            for (result in data.toObjects(Post::class.java)) {
                val post = Post(result.exclusive,)
                post.title = result.title
                post.content = result.content
                post.imageUrl = result.imageUrl
                post.timestamp = result.timestamp

                tempList.add(post)
            }
            _exclusivePosts.value = tempList
        } catch (e: Exception) {
            throw PostRetrievalError("Volgende ging mis: ${e}")
        }
    }

    suspend fun getAllNonExclusivePostsForUsers() {
        try {
            val tempList = arrayListOf<Post>()

            val data = postRef
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .whereGreaterThanOrEqualTo("timestamp", Timestamp.now().toDate())
                .whereEqualTo("exclusive", false)
                .get()
                .await()

            for (result in data.toObjects(Post::class.java)) {
                val post = Post(result.exclusive,)
                post.title = result.title
                post.content = result.content
                post.imageUrl = result.imageUrl
                post.timestamp = result.timestamp

                tempList.add(post)
            }
            _nonExclusivePosts.value = tempList
        } catch (e: Exception) {
            throw PostRetrievalError("Volgende ging mis: ${e}")
        }
    }

     fun clearPosts() {
        _posts.value = emptyList()
    }
    class PostRetrievalError(message: String) : Exception(message)
}