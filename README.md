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
You can simply add interceptor in your client and shake your device to see screen with all the API calls.

Example:
Add in your Retrofit client
```kotlin
val client = OkHttpClient.Builder()
    client.addInterceptor(DebugInterceptor.create())
        .build()
```

To use shake device feature add this line in your activity/fragment
```kotlin
DebugInterceptor.start(WeakReference(this))
```

If you do not want to use shake device feature and would like to open screen manually then:
```kotlin
val myIntent = Intent(this, LogsActivity::class.java)
myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
startActivity(myIntent)
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

Support
-------
[![](https://giphy.com/gifs/buymeacoffee-creator-buy-me-a-coffee-support-7kZE0z52Sd9zSESzDA)](https://www.buymeacoffee.com/rrohaill)

License
-------
Copyright 2022 Rohail

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
