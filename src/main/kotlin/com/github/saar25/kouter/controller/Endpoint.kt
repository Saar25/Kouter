package com.github.saar25.kouter.controller

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Endpoint(val endpoint: String = "")
