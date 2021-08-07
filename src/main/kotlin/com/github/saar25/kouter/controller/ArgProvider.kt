package com.github.saar25.kouter.controller

import com.github.saar25.kouter.RouteInput
import kotlin.reflect.KParameter

interface ArgProvider<T> {
    fun test(parameter: KParameter): Boolean
    fun provide(parameter: KParameter, input: RouteInput<T>): Any?
}