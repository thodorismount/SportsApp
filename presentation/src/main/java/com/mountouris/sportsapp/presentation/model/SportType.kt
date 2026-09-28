package com.mountouris.sportsapp.presentation.model

import androidx.annotation.DrawableRes
import com.mountouris.sportsapp.presentation.R

/**
 * Represents a known sport category, mapping the API sport ID to its corresponding icon.
 *
 * Each enum name matches the sport ID returned by the API. Use [fromId] to resolve an ID
 * to its entry; unknown IDs resolve to [UNKNOWN], which carries a null [iconRes].
 *
 * @property iconRes Drawable resource for the sport's icon, or null if no icon is available.
 */
enum class SportType(@DrawableRes val iconRes: Int?) {
    FOOT(R.drawable.ic_sport_soccer),
    FUTS(R.drawable.ic_sport_soccer),
    BASK(R.drawable.ic_sport_basketball),
    TENN(R.drawable.ic_sport_tennis),
    TABL(R.drawable.ic_sport_tennis),
    VOLL(R.drawable.ic_sport_volleyball),
    ESPS(R.drawable.ic_sport_esports),
    ICEH(R.drawable.ic_sport_hockey),
    HAND(R.drawable.ic_sport_handball),
    SNOO(R.drawable.ic_sport_snooker),
    DART(R.drawable.ic_sport_darts),
    UNKNOWN(null);
}

/** Returns the [SportType] whose name matches [sportId], or [SportType.UNKNOWN] if not recognized. */
fun sportTypeFromId(sportId: String): SportType =
    SportType.entries.find { it.name == sportId } ?: SportType.UNKNOWN
