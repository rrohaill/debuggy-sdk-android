debuggy-sdk-android
-------------
Debug tool for network calls

[![](https://jitpack.io/v/rrohaill/debuggy-sdk-android.svg)](https://jitpack.io/#rrohaill/debuggy-sdk-android)

Introduction
--------
* Problem: 
Many times testers are in need to check the API calls the app is using. Sometimes to debug an issue the non-dev testers don’t know if the issue belongs to the front-end or backend. It would be easier for non-devs to quickly check API calls and see requests and responses to understand if it is actually an issue or expected app behaviour.

There are some third-party libraries that we can use to display a screen with all the API calls within the application. But the problem occurs with security. Some libraries are redirecting API calls to their web servers and then to our backend which we are not fully comfortable with.

* Solution: 
To solve the above problem, we have built a library and naming it “Debug tool or Debuggy”.

Usage
--------
```kotlin
val client = OkHttpClient.Builder()
    client.addInterceptor(DebugInterceptor.create())
        .build()
```

Download
--------

Gradle:
Step 1. Add the JitPack repository to your build file.
Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
```
Step 2. Add the dependency
```groovy
dependencies {
    implementation 'com.github.rrohaill:debuggy-sdk-android:0.0.3'
}
```

Maven:
Step 1. Add the JitPack repository to your build file
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
Step 2. Add the dependency
```xml
<dependency>
    <groupId>com.github.rrohaill</groupId>
    <artifactId>debuggy-sdk-android</artifactId>
    <version>0.0.3</version>
</dependency>
```
