package com.mountouris.sportsapp.domain.usecase

import com.mountouris.sportsapp.domain.model.Sport
import kotlinx.coroutines.flow.Flow

/**
 * Fetches sports from the remote source and merges each event's favorite status
 * from the local source. Emits a new value whenever favorites change.
 */
interface GetSportsWithFavoritesUC {

    /**
     * Returns a [Flow] that emits [Result.success] with the merged list on success,
     * or [Result.failure] if the initial network fetch fails.
     */
    fun execute(): Flow<Result<List<Sport>>>
}
