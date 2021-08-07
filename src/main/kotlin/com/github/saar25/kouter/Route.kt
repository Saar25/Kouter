package com.github.saar25.kouter

internal data class Route<T>(
    val path: String,
    val handler: RouteHandler<T>,
)