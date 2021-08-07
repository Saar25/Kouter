package com.github.saar25.kouter

fun interface RouteHandler<T> {
    fun handle(input: RouteInput<T>)
}