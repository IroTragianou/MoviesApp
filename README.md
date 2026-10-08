# MoviesApp

An Android movie discovery application developed in Java, using the TMDB API to retrieve movie information.

## Features

- Browse movies retrieved from TMDB.
- View movie details, genres, and cast information.
- Read movie reviews.
- Discover similar movies.
- Save favorite movies locally.
- Browse a layout designed for Android devices.

## Tech Stack

| Technology | Purpose |
|---|---|
| Java | Application development |
| Android SDK | Android platform |
| MVVM | UI architecture |
| Retrofit | REST API communication |
| Room | Local database |
| Dagger | Dependency injection |
| TMDB API | Movie data |
| Gradle | Build automation |

## Architecture

The application separates responsibilities into UI, domain, and data layers.

- **UI:** Activities, Fragments, ViewModels, and Adapters.
- **Domain:** Use cases, domain models, and repository interfaces.
- **Data:** API services, remote models, local database, and repository implementations.
- **Dependency Injection:** Dagger modules for configuring dependencies.

## Getting Started

1. Clone the repository.
2. Open the project in Android Studio.
3. Obtain an API key from [The Movie Database (TMDB)](https://www.themoviedb.org/settings/api).
4. Add the following property to your local `local.properties` file:

   `TMDB_API_KEY=YOUR_TMDB_API_KEY`

5. Sync the project with Gradle.
6. Run the application on an Android device or emulator.

**Security note:** The API key is not committed to the repository. The application reads it from `local.properties` during the build process.

## Screenshots

Screenshots of the application's main screens will be added here.

## Project Background

This project demonstrates practical Android development with Java, API integration, local persistence, dependency injection, and layered application architecture.

## Data Attribution

Movie information is provided by [The Movie Database (TMDB)](https://www.themoviedb.org/).

This product uses the TMDB API but is not endorsed or certified by TMDB.<img width="691" height="1536" alt="Εικόνα ChatGPT 8 Οκτ 2026, 06_11_02 μ μ" src="https://github.com/user-attachments/assets/41076650-a426-4fcc-9863-c1c888db8070" />
<img width="691" height="1536" alt="Εικόνα ChatGPT 8 Οκτ 2026, 06_11_10 μ μ" src="https://github.com/user-attachments/assets/2bb73a3a-c498-454e-8b3b-f3f1c9713fa3" />
<img width="691" height="1536" alt="Εικόνα ChatGPT 8 Οκτ 2026, 06_11_20 μ μ" src="https://github.com/user-attachments/assets/2268d2f7-bd5b-48dc-8e06-ca85c5abffcf" />
<img width="691" height="1536" alt="Εικόνα ChatGPT 8 Οκτ 2026, 06_11_29 μ μ" src="https://github.com/user-attachments/assets/b8c00eed-eeb6-4156-ae14-fa6411153bc2" />
