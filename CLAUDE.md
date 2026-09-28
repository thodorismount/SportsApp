# SportsApp

## Project Overview
Android sports events app. Displays upcoming events grouped by sport, with favorites, countdown timers, and collapse/expand per sport.

## Architecture
Clean Architecture + MVVM with a multi-module api/impl split. Only `:app` (composition root) depends on impl modules.

- **`:domain:api`** → domain models (`Sport`, `SportEvent`), repository interface, use case interfaces
- **`:domain:impl`** → use case implementations
- **`:remote:api`** → `SportsApiSource` interface (returns domain models)
- **`:remote:impl`** → Ktor client, DTOs, JSON deserialisation, DTO→domain mappers (internal)
- **`:local:api`** → `LocalFavoritesSource` interface
- **`:local:impl`** → Room database, `FavoriteEntity`, `FavoriteDao`
- **`:data`** → `SportsRepositoryImpl` — combines remote + local sources
- **`:presentation`** → ViewModel, UI models, Compose screens & components
- **`:app`** → Koin DI modules, `MainActivity`

## Tech Stack
- **UI**: Jetpack Compose + Material 3
- **DI**: Koin
- **Networking**: Ktor (NOT Retrofit)
- **Local DB**: Room
- **Async**: Kotlin Coroutines + Flow
- **Serialization**: kotlinx.serialization

## API
Endpoint: https://thodorismount.github.io/SportsApp/docs/sports.json
Response: List of sports. Each sport has `id`, `name`, and `events`.
Event fields: `id`, `sportId`, `homeTeam`, `awayTeam`, `startTime` (unix seconds).

The JSON is a static file served via GitHub Pages from `docs/sports.json` in this repo. To refresh sample data, edit that file and push to `main`.

## Key Behaviors
- Events grouped by sport in a LazyColumn
- Each sport header: sport-specific icon, sport name, favorites-filter toggle (star icon), expand/collapse chevron
- Events displayed as horizontal LazyRow of cards per sport
- Each card: countdown timer (HH:MM:SS, updates every second), star favorite button, competitor1, "VS" (AccentRed), competitor2
- Countdown driven by a single ticker in the ViewModel updating currentTimeSeconds in UiState
- Favorites stored in Room DB, toggling them triggers reactive UI update via Flow
- Expanded/filter state preserved across Flow re-emissions (favorites changes should NOT reset UI state)
- Empty state and error state screens required
- Dark/light theme toggle via sun/moon icon button in the top app bar (defaults to dark)

## Sport Icons
Sport icons are vector drawables in `presentation/res/drawable/`. The `SportType` enum (in `:presentation`) maps API sport IDs to icon resources — each entry name matches the API sport ID. `SportType.UNKNOWN` has a null `iconRes`; the header renders no icon in that case. Mapping happens in `UiMappers.kt`, not in composables.

## Build & Test Commands

```bash
# Build
./gradlew assembleDebug

# Run all unit tests
./gradlew test

# Run tests for a specific module
./gradlew :domain:impl:test
./gradlew :data:test
./gradlew :presentation:test

# Lint
./gradlew lint
```

Prefer running a single module's tests rather than the full suite when working in a specific layer.

## Coding Conventions
- Idiomatic Kotlin: use mapNotNull, takeIf, expression bodies, extension functions
- Minimal comments — code should be self-explanatory
- Use // region / // endregion for logical grouping in longer files
