package com.example.tweetviewer.viewmodel

import com.example.tweetviewer.models.Tweet

// 1. Sealed Class: Represents the 3 exact states our UI can be in.
// "data object" is the modern Kotlin way to declare objects without parameters.
sealed class UiState {
    data object Loading : UiState()
    data class Success(val tweets: List<Tweet>) : UiState()
    data class Error(val message: String) : UiState()
}