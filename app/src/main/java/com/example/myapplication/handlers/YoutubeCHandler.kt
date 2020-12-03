package com.example.myapplication.handlers

import com.example.myapplication.models.Youtube
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class YoutubeCHandler {
    var database: FirebaseDatabase
    var youtubeRef: DatabaseReference

    init {
        database = FirebaseDatabase.getInstance()
        youtubeRef = database.getReference("Youtube Channels")
    }

    fun create(youtube: Youtube): Boolean {
        val id = youtubeRef.push().key
        youtube.id = id

        youtubeRef.child(id!!).setValue(youtube)
        return true
    }

    fun update(youtube: Youtube): Boolean {

        youtubeRef.child(youtube.id!!).setValue(youtube)
        return true
    }

    fun delete(youtube: Youtube): Boolean {
        youtubeRef.child(youtube.id!!).removeValue()
        return true
    }
}