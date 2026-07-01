# FLUX

FLUX is an Android application for discovering movies and TV shows, searching a global database, and managing personal watch lists. 

## Features

* Discover Content: Browse movies and TV shows across Popular, Top Rated, and Upcoming categories.
* Smart Search: Look up titles using a real-time search engine.
* Personalized Lists: Manage titles in Watchlist and Watched sections.
* Dynamic Action Cards: View ratings, expand cards for descriptions, and save titles.

## Tools Used

* Kotlin: Programming language.
* Jetpack Compose: Native user interface toolkit.
* Room Database: Local SQLite storage for saving lists on the device.
* Retrofit: HTTP client for fetching data from the internet.
* Gson Converter: Parses raw JSON text into Kotlin objects.
* Coil: Loads movie posters.
* Jetpack Navigation: Manages transitions between app screens.

## Architecture

The app follows the MVVM (Model-View-ViewModel) pattern along with a Repository pattern to coordinate data between the internet API and local storage.

## Setup

1. Clone the repository.
2. Get an API Key from The Movie Database (TMDB).
3. Add the key as TMDB_API_KEY in your local properties.
4. Build and run the project using Android Studio.
