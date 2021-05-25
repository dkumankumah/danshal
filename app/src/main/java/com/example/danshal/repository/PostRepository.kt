package com.example.danshal.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.danshal.models.Post
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class PostRepository {
    private val db = Firebase.firestore
    private val postRef = db.collection("posts")

    private val _posts: MutableLiveData<List<Post>> = MutableLiveData()
    private val _exclusivePosts: MutableLiveData<List<Post>> = MutableLiveData()
    private val _nonExclusivePosts: MutableLiveData<List<Post>> = MutableLiveData()

    val posts: MutableLiveData<List<Post>>
        get() = _posts

    suspend fun getAllPosts() {
        try {
            val tempList = arrayListOf<Post>()

            val data = postRef
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .await()

            for (result in data.toObjects(Post::class.java)) {
                val post = Post(result.exclusive)
                post.title = result.title
                post.content = result.content
                post.imageUrl = result.imageUrl
                post.timestamp = result.timestamp
                post.id = result.id

                tempList.add(post)
            }
            _posts.value = tempList
        } catch (e: Exception) {
            throw PostRetrievalError("Volgende ging mis: ${e}")
        }
    }

    fun removePost(doc: String) {
        try {
            if (doc.isNotEmpty()) {
                postRef.document(doc)
                    .delete()
            } else {
                throw PostRetrievalError("Id is niet gevonden")
            }
        } catch (e: Exception) {
            throw PostRetrievalError("Volgende ging mis: ${e}")
        }
    }

    fun updatePost(post: Post) {
        try {
            postRef
                .document(post.id)
                .update(
                    mapOf(
                        "content" to post.content,
                        "exclusive" to post.exclusive,
                        "imageUrl" to post.imageUrl,
                        "title" to post.title,
                        "timestamp" to FieldValue.serverTimestamp()
                    )
                )
        }catch (e: Exception) {
            throw PostRetrievalError("Volgende ging mis: ${e}")
        }
    }

    // The following methods are for the users (fetch posts with date from today)
    suspend fun getAllPostsForUsers() {
        _posts.value = emptyList()
        try {
            val tempList = arrayListOf<Post>()

            val data = postRef
                .orderBy("timestamp", Query.Direction.DESCENDING)
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

    suspend fun getAllNonExclusivePostsForUsers() {
        _posts.value = emptyList()
        try {
            val tempList = arrayListOf<Post>()

            val data = postRef
                .orderBy("timestamp", Query.Direction.DESCENDING)
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
            _posts.value = tempList
        } catch (e: Exception) {
            throw PostRetrievalError("Volgende ging mis: ${e}")
        }
    }

    class PostRetrievalError(message: String) : Exception(message)
}