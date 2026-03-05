package com.example.tweetviewer.models

import com.google.gson.annotations.SerializedName

// Wrapper for the entire JSONBin response
data class TweetsResponse(
    @SerializedName("record")
    val record: Record
)

// Wrapper for the 'record' object inside the JSON
data class Record(
    @SerializedName("tweets")
    val tweets: List<Tweet>
)

// The actual Tweet data we care about
data class Tweet(
    @SerializedName("category")
    val category: String,

    @SerializedName("text")
    val text: String
)