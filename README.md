[![](https://jitpack.io/v/green-nick/Result.svg)](https://jitpack.io/#green-nick/Result)
# Result
Small class and extensions that are used as some result holder.
In fact this is Either monad implementation, but oriented on "results".
Unlike Kotlin's default Result could be used without any compiler flags and error part could be any object.

## Usage
### Initialization:
Representing success result:  
`val success: Result<String, Nothing> = Result.success("hello")`

Representing success result without return type:  
`val success: Result<Unit, Nothing> = Result.success()`

Representing error:  
`val error: Result<Nothing, Throwable> = Result.error(IllegalStateException("wrong action))`

try-catch replacement:  
```
val result: Result<Int, Throwable> = tryWithResult {
    "hello".toInt()
}
result.isFailure == true
result.error() == NumberFormatException
```
It'll catch all thrown `Throwable`s

Because of using `Nothing` as representation of missing type, you can safely cast `Result` to any needed type:
```
fun loadUser(): Result<User, Throwable> {
   val result: Result<User, Nothing> = Result.success(User())
   return result // this works fine despite function requires Result<User, Throwable> return type
}
```

### Getting result:
There are few ways to get result from `Result` object.  
Native are:
```
val success = Result.success("hello")
success.getOrThrow() == "hello"

val error = Result.error("critical error")
error.errorOrThrow() == "critical error"
```
But be aware that these functions throw `IllegalStateException` if you try to get inappropriate result.  
To prevent this you can use checks:  
`result.isSuccess` and `result.isFailure`

Besides that you can use another extensions to unwrap value:
```
val result: Result<String, Throwable> = ...

val name = result.get() // safe, returns nullable
val error = result.error() // safe, returns nullable

val (name, error) = result // destructuring declaration, safe, nullable

// these are safe and returns Result itself:
result.onSuccess { name -> println(name) }
    .onError { error -> println(error) }
    .withSuccess { println(this) }
    .withError { println(this) }
    .getOrDefault { "hello" }
    .errorOrDefault { IllegalStateException() }
 
 result.getOrDefault("hello") // safe, returns default if error
 result.errorOrDefault(IllegalStateException()) // safe, returns default if success

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