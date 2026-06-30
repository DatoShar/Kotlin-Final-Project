# FLUX

**FLUX** is a modern, lightweight Android application designed for discovering movies and TV shows, searching a global database, and managing personal watch lists. Built entirely with a native, declarative user interface, the app provides a smooth and highly responsive tracking experience.

---

## 🚀 Features

* **Discover Content:** Browse through curated sections of movies and TV shows including Popular, Top Rated, and Upcoming releases.
* **Smart Search:** Quickly look up your favorite titles via a dedicated, real-time search engine.
* **Personalized Lists:** Keep track of what you want to watch and what you have already seen using the simplified **Watchlist** and **Watched** tabs.
* **Dynamic Action Cards:** View ratings, toggle expandable movie cards to read descriptions, and quickly categorize media with a single click.

---

## 🛠️ Tools & Tech Stack

FLUX leverages modern Android development tools and libraries to ensure efficiency, data persistence, and seamless network communication.

| Tool / Library | Purpose |
| :--- | :--- |
| **Kotlin** | The primary, modern programming language used for the entire codebase. |
| **Jetpack Compose** | Android’s modern toolkit for building native, declarative user interfaces. |
| **Room Database** | A robust abstraction layer over SQLite used to save your lists directly on the device's local storage. |
| **Retrofit** | A type-safe HTTP client used to seamlessly request and fetch movie data from the internet. |
| **Gson Converter** | Handles the automatic parsing of raw JSON responses from the server into clean Kotlin data classes. |
| **Coil** | An image loading library backed by Kotlin Coroutines to asynchronously load movie posters. |
| **Jetpack Navigation** | Manages the seamless transitions and argument-passing between different app screens. |

---

## 🏗️ Architecture

The app is built using the **MVVM (Model-View-ViewModel)** architectural pattern coupled with a **Repository Pattern**. 

* **UI Layer (Compose):** Handles rendering the data to the screen based on the current state.
* **ViewModel Layer:** Preserves app state and encapsulates the business logic required by the screens.
* **Repository Layer:** Acts as a mediator that decides whether to pull dynamic data from the live internet API or fetch saved entities from the local database.

---

## 🔑 Setup & Configuration

To get this project running locally, you need to plug in your personal API credentials:

1. Clone or download the repository.
2. Obtain an API Key from **The Movie Database (TMDB)**.
3. Add your API key to your local configuration (e.g., inside your environment variables or `local.properties` as `TMDB_API_KEY`).
4. Sync, build, and run the project using **Android Studio**.
