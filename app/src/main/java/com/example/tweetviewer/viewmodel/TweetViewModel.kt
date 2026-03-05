package com.example.tweetviewer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tweetviewer.models.Tweet
import com.example.tweetviewer.repository.TweetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TweetViewModel @Inject constructor(
    private val repository: TweetRepository // Hilt injects this automatically!
) : ViewModel() {

    // 2. THE CORRECT WAY: Convert Cold Flow to Hot StateFlow without an init{} block
    val uiState: StateFlow<UiState> = repository.getTweets()
        .map { result ->
            // Transform the raw Result into our clean UiState
            if (result.isSuccess) {
                UiState.Success(result.getOrDefault(emptyList()))
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "An unknown error occurred")
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Auto-pauses in background to save battery
            initialValue = UiState.Loading // Start with Loading state instantly
        )

    // 3. Category Management
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    // 4. Combine: Reactively merge the API Data and the User's Category choice
    val filteredTweets: StateFlow<List<Tweet>> = combine(uiState, selectedCategory) { state, category ->
        if (state is UiState.Success) {
            if (category == "All") {
                state.tweets // Show everything
            } else {
                state.tweets.filter { it.category == category } // Filter by category
            }
        } else {
            emptyList() // If loading or error, there are no tweets to filter
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}