# Kouter

Kotlin Path Router

A lightweight library that helps you connect route paths to a callback  
Making it very easy to write an event driven application

### Using Kouter with Maven
```xml
<dependency>
  <groupId>com.github.saar25</groupId>
  <artifactId>kouter</artifactId>
  <version>1.0.0</version>
</dependency>
```

### Using Kouter with Gradle

```kotlin
dependencies {
    implementation('com.github.saar25:kouter:1.0.0')
}
```

## Router

The `Router<T>` class groups together multiple `RouteHandler`s  
It can handle events and redirect them to the correct route

```kotlin
val router = Router<String>()
    .route("/a") { println("Route to /a, param: ${it.data}") }
    .route("/b") { println("Route to /b, param: ${it.data}") }
    .route("/c") { println("Route to /c, param: ${it.data}") }
    .route("/") { println("Route to unknown: ${it.path}") }

router.handle(RouteInput("/a", "This is param"))
// prints: 'Route to /a, param: "This is param"'
```

Routers can also belong to other routers in order to build modular routes

```kotlin
val anotherRouter: Router<String> = ...

router.route("/other", anotherRouter)
```

## Controller

Another way to create routers is via a `Controller`  
Controllers use annotations in order to receive data and export endpoints

```kotlin
class MyController : Controller {
    @Endpoint
    fun triangle(@Data data: String) {
        if (data == "up") {
            println("   *   ")
            println("  * *  ")
            println(" *   * ")
            println("*******")
        } else {
            println("*******")
            println(" *   * ")
            println("  * *  ")
            println("   *   ")
        }
    }

    @Endpoint
    fun square() {
        println("*******")
        println("*     *")
        println("*     *")
        println("*******")
    }
}

val router = MyController().buildRouter<String>()

router.handle(RouteInput("triangle", "down"))
// prints an upside down triangle

router.handle(RouteInput("square", ""))
// prints a square
```

### Creating annotations

Using your own annotations can have many benefits  
for example supplying more arguments or parsing your data

```kotlin
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class IsUp

val isUpArgProvider = object : ArgProvider<String> {
    override fun test(parameter: KParameter): Boolean {
        return parameter.annotations.any { it is IsUp }
    }

    override fun provide(parameter: KParameter, input: RouteInput<String>): Any {
        return input.data == "up"
    }
}

val router = MyController().buildRouter(listOf(isUpArgProvider))
```

And now triangle endpoint can be written differently

```kotlin
@Endpoint
fun triangle(@IsUp isUp: Boolean) {
    if (isUp) {
        println("   *   ")
        println("  * *  ")
        println(" *   * ")
        println("*******")
    } else {
        println("*******")
        println(" *   * ")
        println("  * *  ")
        println("   *   ")
    }
}
```

## Examples

### Console input

```kotlin
val router: Router<String> = ...

var line = readLine()
while (line != null && line != "exit") {
    val split = line.split(" ")

    val path = split[0]
    val arg = split.getOrElse(1) { "" }

    router.handle(RouteInput(path, arg))

    line = readLine()
}
```

### Http server

using the package com.sun.net.httpserver

```kotlin
val router: Router<HttpExchange> = ...

val server = HttpServer.create(InetSocketAddress(8000), 0)
server.createContext("/") { t ->
    router.handle(RouteInput(t.requestURI.path, t))
}
server.start()
```