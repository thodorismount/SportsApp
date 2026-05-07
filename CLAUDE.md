# SportsApp — Kaizen Gaming Assessment

## Project Overview
Android sports events app. Displays upcoming events grouped by sport, with favorites, countdown timers, and collapse/expand per sport.

## Architecture
Clean Architecture + MVVM:
- **data/** → DTOs, Room entities, API source, repository impl, mappers
- **domain/** → models, repository interface, use cases
- **presentation/** → ViewModel, UI models, Compose screens & components
- **di/** → Koin modules

## Tech Stack
- **UI**: Jetpack Compose
- **DI**: Koin
- **Networking**: Ktor (NOT Retrofit)
- **Local DB**: Room
- **Async**: Kotlin Coroutines + Flow
- **Serialization**: kotlinx.serialization

## API
Endpoint: https://ios-kaizen.github.io/MockSports/sports.json
Response: List of sports, each with id ("i"), name ("d"), and events ("e").
Event fields: id ("i"), sportId ("si"), name ("d"), startTime unix seconds ("tt").
Event name format: "Competitor1-Competitor2" — split on "-" for display.

## Key Behaviors
- Events grouped by sport in a LazyColumn
- Each sport header: sport name, favorites-filter toggle (star icon), expand/collapse chevron
- Events displayed as horizontal LazyRow of cards per sport
- Each card: countdown timer (HH:MM:SS, updates every second), star favorite button, competitor1, competitor2
- Countdown driven by a single ticker in the ViewModel updating currentTimeSeconds in UiState
- Favorites stored in Room DB, toggling them triggers reactive UI update via Flow
- Expanded/filter state preserved across Flow re-emissions (favorites changes should NOT reset UI state)
- Empty state and error state screens required

## Coding Conventions
- Idiomatic Kotlin: use mapNotNull, takeIf, expression bodies, extension functions
- No runBlocking anywhere
- Minimal comments — code should be self-explanatory
- Use // region / // endregion for logical grouping in longer files