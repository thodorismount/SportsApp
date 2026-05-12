# SportsApp — Kaizen Gaming Android Assessment

An Android app that fetches upcoming sports events from a remote API, groups them by sport, and lets users manage favorites — all built with Clean Architecture, MVVM, and a fully modularised Gradle setup.

---

## Screenshots

| Light Theme | Dark Theme |
|:-----------:|:----------:|
| <img src="screenshots/light_theme.png" width="280"/> | <img src="screenshots/dark_theme.png" width="280"/> |

---

## Architecture

The project follows **Clean Architecture** with a strict separation of concerns across layers, combined with an **api/impl module split** that enforces dependency inversion at the Gradle level: only `:app` (the composition root) knows about implementation classes.

```
┌─────────────────────────────────────────────┐
│                    :app                     │  Composition root — wires everything via Koin
├──────────────┬──────────────────────────────┤
│ :presentation│                              │  ViewModel, UI models, Compose screens
├──────────────┤          :data               │  SportsRepository implementation
│ :domain:api  │                              │  Domain models, repository interface, use cases (interfaces)
│ :domain:impl │                              │  Use case implementations
├──────────────┼──────────────┬───────────────┤
│  :remote:api │ :remote:impl │  :local:api   │  :local:impl
│  (interface) │  Ktor + DTOs │  (interface)  │  Room + DAOs
└──────────────┴──────────────┴───────────────┘
```

### Module responsibilities

| Module | Role |
|---|---|
| `:domain:api` | Domain models (`Sport`, `SportEvent`), repository interface, use case interfaces |
| `:domain:impl` | Use case implementations (`GetSportsWithFavoritesUCImpl`, `ToggleFavoriteUCImpl`) |
| `:remote:api` | `SportsApiSource` interface — exposes domain models, hides network details |
| `:remote:impl` | Ktor HTTP client, DTOs, JSON deserialisation, DTO→domain mappers |
| `:local:api` | `LocalFavoritesSource` interface — exposes `Flow<Set<String>>` and `toggleFavorite` |
| `:local:impl` | Room database, `FavoriteEntity`, `FavoriteDao`, source implementation |
| `:data` | `SportsRepositoryImpl` — combines remote and local sources |
| `:presentation` | ViewModel, UI models (`SportModel`, `EventModel`), Compose screens and components |
| `:app` | Koin DI modules, `MainActivity` |

---

## Tech Stack

| Concern | Library |
|---|---|
| UI | Jetpack Compose + Material 3 |
| Architecture | ViewModel + StateFlow |
| Dependency Injection | Koin 3.5 |
| Networking | Ktor 2.3 (Android engine) |
| Serialisation | kotlinx.serialization |
| Local persistence | Room 2.6 |
| Async | Kotlin Coroutines + Flow |
| Testing | JUnit 4, MockK, Turbine, kotlinx-coroutines-test |

---

## Key Features

- **Events grouped by sport** in a `LazyColumn`, each sport in its own collapsible section
- **Favorites** — toggled per event, persisted in Room, observed reactively via `Flow`
- **Countdown timer** — `HH:MM:SS` per event card, driven by a single ticker in the ViewModel updating every second
- **Favorites filter** — per-sport toggle that hides non-favourite events without losing expanded/filter state on data refresh
- **Error & empty states** — dedicated screens with a retry action
- **Dark theme** throughout

---

## Unit Tests

Tests follow a consistent `given / when / then` structure with MockK for mocking and Turbine for Flow assertions. Coverage spans every layer:

| Module | Tests |
|---|---|
| `:domain:impl` | `GetSportsWithFavoritesUCImplTest`, `ToggleFavoriteUCImplTest` |
| `:remote:impl` | `MappersTest` — DTO→domain mapping |
| `:data` | `SportsRepositoryImplTest` — delegation, `runCatching` wrapping |
| `:presentation` | `UiMappersTest` — domain→UI mapping; `SportsViewModelTest` — state transitions |

---

## Extras

### Dark / light theme toggle
A sun/moon icon button in the top app bar lets the user switch between the custom dark and light colour schemes at runtime. The preference is held in `rememberSaveable` in `MainActivity` so it survives rotation. Both schemes use the same Kaizen brand colours (Gold, PrimaryBlue, AccentRed) with appropriate background and surface adaptations for each mode.

### Sport category icons
Each sport section header displays a sport-specific icon loaded from a vector drawable stored in `res/drawable`. The mapping from API sport ID to icon is handled by the `SportType` enum — each entry's name matches the API sport ID and carries a nullable `@DrawableRes`. An `UNKNOWN` sentinel entry covers unrecognised IDs gracefully (no icon rendered). The mapping lives in `UiMappers.kt` and is fully covered by unit tests.
