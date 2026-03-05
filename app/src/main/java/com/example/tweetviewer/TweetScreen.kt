package com.example.tweetviewer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tweetviewer.models.Tweet
import com.example.tweetviewer.viewmodel.TweetViewModel
import com.example.tweetviewer.viewmodel.UiState

@Composable
fun TweetScreen(viewModel: TweetViewModel = hiltViewModel()) {

    // 1. collectAsStateWithLifecycle() ensures we don't leak memory when the app is in the background
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filteredTweets by viewModel.filteredTweets.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(top = 32.dp)) {

        Text(
            text = "Tweets",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        // 2. Handle the 3 States cleanly
        when (val state = uiState) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                }
            }

            is UiState.Success -> {
                // CORRECT SOLUTION: remember() prevents recalculating categories on every UI frame
                val categories = remember(state.tweets) {
                    listOf("All") + state.tweets.map { it.category }.distinct()
                }

                // 3. Category Filter Row
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        CategoryChip(
                            category = category,
                            isSelected = category == selectedCategory,
                            onClick = { viewModel.setCategory(category) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 4. Tweet List
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Using text.hashCode() as a Key improves scroll performance and prevents recomposition bugs!
                    items(filteredTweets, key = { tweet -> tweet.text.hashCode() }) { tweet ->
                        TweetCard(tweet)
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryChip(category: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = category,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun TweetCard(tweet: Tweet) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = tweet.text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "#${tweet.category}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}