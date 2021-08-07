package com.github.saar25.kouter

data class RouteInput<T>(
    val path: String,
    val data: T,
)