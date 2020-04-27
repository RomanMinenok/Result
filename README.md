[![](https://jitpack.io/v/green-nick/Result.svg)](https://jitpack.io/#green-nick/Result)
# Result
Small class that is used as result holder (Success or Failure) and extensions on top of it.
In fact this is Either monad implementation, but instead of "Right" and "Left" it contains of "Success" and "Failure".
Unlike Kotlin's default Result could be used without any compiler flags and any object could be used as error part.

## Usage
### Initialization:
Representing success result:  
`val success: Result<String, Nothing> = Result.success("hello")`

Representing success result without return type:  
`val success: Result<Unit, Nothing> = Result.success()`

Representing error:  
`val error: Result<Nothing, Throwable> = Result.error(IllegalStateException("wrong action))`

try-catch replacement:  
```kotlin
val result: Result<Int, Throwable> = tryWithResult {
    "hello".toInt()
}
result.isFailure == true
result.error() == NumberFormatException
```
It'll catch all thrown `Throwable`s

Because of using `Nothing` as representation of missing type, you can safely cast `Result` to any needed type:
```kotlin
fun loadUser(): Result<User, Throwable> {
   val result: Result<User, Nothing> = Result.success(User())
   return result // this works fine despite function requires Result<User, Throwable> return type
}
```

### Getting result:
There are few ways to get result from `Result` object.  
Native are:
```kotlin
val success = Result.success("hello")
success.getOrThrow() == "hello"

val error = Result.error("critical error")
error.errorOrThrow() == "critical error"
```
But be aware that these functions throw `IllegalStateException` if you try to get inappropriate result.  
To prevent this you can use checks:  
`result.isSuccess` and `result.isFailure`

Besides that you can use another extensions to unwrap value:
```kotlin
val result: Result<String, Throwable> = loadUserName()

val name = result.get() // safe, returns nullable
val error = result.error() // safe, returns nullable

val (name, error) = result // destructuring declaration, safe, nullable

// these are safe and returns Result itself:
result.onSuccess { name -> println(name) }
    .onError { error -> println(error) }
    .withSuccess { println(this) }
    .withError { println(this) }
 
 result.getOrDefault { "hello" } // safe, returns default if error
 result.getOrDefault("hello") // safe, returns default if error
 
 result.errorOrDefault { IllegalStateException() } // safe, returns default if success
 result.errorOrDefault(IllegalStateException()) // safe, returns default if success
```

There are some extensions to work with Lists:
```kotlin
val result: Result<String, Throwable> = loadUserName()

val list: List<String> = result.toList() // returns List with single success element or empty list
val listOfError: List<Throwable> = result.toErrorList() // works opposite to above
```

Let's imagine we have some list of Results. 
If you need, you can map and filter it into list of success or failed items:
```kotlin
val results: List<Result<String, Throwable>> = loadUserNames()

val successItems: List<String> = results.toSuccessList()
val failedItems: List<Throwable> = results.toErrorList()
```
### Modifications:
You can use such modifiers as:  
`map`, `flatMap`, `mapError`, `flatMapError`, `swap`.

## Other things:
`doIfSuccess` and `doIfError` are called in appropriate cases, but without values.  
Replacement of `if (result.isSuccess) { }` and `if (result.isFailure) { }`

## How to add to your project:
**Step 1.** Add the JitPack repository to your build file.  
Add this in your module's build.gradle at the end of repositories:  
```
repositories {
    maven { url 'https://jitpack.io' }
}
```
**Step 2.** Add the dependency
```
dependencies {
    implementation "com.github.green-nick:Result:{put latest version here}"
}
```
