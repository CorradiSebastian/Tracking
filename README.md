# Traking
### tracking app, created in kotlin

This app was created to show some basic implementations of android concepts, libraries and architectures described below.

Attempts to show basic implementations of Foreground Services, Compose, Location libraries and firebase RTDM.

launches a foreground service to request the location periodically, saves it in a firebase database and displays it on a map.

In some future it will allow you to create fleets and view them in real time.

All with reactive programming (kotlin flows), MVVM, Compose and firebase.

**NOT AVAILABLE ON GOOGLE PLAY YET**

**LIBRARIES**

- FLOWS: A library to manage stream of data that can be computed asynchronously

- Coroutines: Kotlin's coroutines are used to handle several threads, it is well known that you cannot do a network call on the main thread.

- Compose: A modern declarative UI Toolkit for Android. Compose makes it easier to write and maintain your app UI by providing a declarative API that allows you to render your app UI without imperatively mutating frontend views.

- Foreground Service: Performs some operation that is noticeable to the user, runs in background even when the app is closed and can be cancelled from the notification bar. In this case it is used to retrieve the location data and street in the cloud.


**ARCHITECTURE**

- CLEAN architecture : Clean architecture is a software design philosophy that separates the elements of a design into ring levels. An important goal of clean architecture is to provide developers with a way to organize code in such a way that it encapsulates the business logic but keeps it separate from the delivery mechanism. Also fits with MVVM, Single Responsibility in Use Cases (Business Logic layer in CLEAN)

- MVVM is used in this app, just a standard implementation interacting with coroutines and composables interacting with viewmodels.

- FIREBASE RTDM: Firebase Realtime Database, a cloud-hosted NoSQL database that lets you store and sync data between your users in realtime. This library was added in order to avoid the development of a backend. And to allow to set fleet data (in the future)


**TO BE ADDED**

- Enhance this description with graphs.

- Fleet feature: add a feature to allow fleet creation, add devices and watch them all in real time.

- App flow: A basic reference of how this app works, how the data is retrived, stored and shown.
 
 - Animations: Just a few to make the UI nicer, it is not the main objective on this app to work on design issues.
 
 - Playstore Link: This app is intented to be uploaded and its code freed.
 
 - Developer contact: My email just to listen suggestions.
 
 **NOT PLANNED YET**

 - Crashlitics:
 
 - Notifications: