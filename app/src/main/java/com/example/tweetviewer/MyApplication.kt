package com.example.tweetviewer

import android.app.Application
import dagger.hilt.EntryPoint
import dagger.hilt.android.HiltAndroidApp

// 1. This annotation generates the base dependency container for your app
@HiltAndroidApp
class MyApplication : Application()