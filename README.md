# DailyTrack

An offline-first productivity and habit tracker for Android, built with Kotlin and Jetpack Compose.

DailyTrack lets you manage daily tasks and habits in one place, see your progress at a glance, and keep all data on-device with Room. No account or network connection required.

## Screenshots

| Home | Tasks | Habits |
|:----:|:-----:|:------:|
| _screenshot coming soon_ | _screenshot coming soon_ | _screenshot coming soon_ |

> Add images to a `screenshots/` folder and reference them here, e.g. `![Home](screenshots/home.png)`.

## Features

- Add, complete and delete tasks
- Add and delete habits, and mark them done for the day
- Daily progress summary
- Persistent local storage with Room
- Reactive UI driven by Kotlin Coroutines and Flow

## Tech stack

| Area | Technology |
|------|------------|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM (ViewModel + Repository) |
| Persistence | Room (DAO, Entities, Database) |
| Async | Kotlin Coroutines, Flow |
| Build | Gradle (Kotlin DSL), JDK 17 |

**Requirements:** compile/target SDK 35, minimum Android 8.0 (API 26).

## Project structure

```
app/src/main/java/com/utkarsh/dailytrack/
├── MainActivity.kt
├── data/
│   ├── Entities.kt
│   ├── DailyTrackDao.kt
│   ├── DailyTrackDatabase.kt
│   └── DailyTrackRepository.kt
└── ui/
    ├── DailyTrackViewModel.kt
    └── DailyTrackViewModelFactory.kt
```

## Getting started

1. Install the latest stable [Android Studio](https://developer.android.com/studio).
2. Clone this repository:
   ```
   git clone <your-repo-url>
   ```
3. Open the project folder in Android Studio and let Gradle sync finish.
4. Run on an emulator or a physical device.

## Roadmap

- Reset habit completion automatically each calendar day
- Reminders and notifications
- Task due dates, filters and search
- Automated tests and accessibility checks
- Onboarding and app icon

## Author

**Utkarsh Kumar**
[GitHub](https://github.com/mkutkarsh24) · [LinkedIn](https://linkedin.com/in/utkarsh-kumar-2b7985345)

Package: `com.utkarsh.dailytrack` · Version `1.0.0`
