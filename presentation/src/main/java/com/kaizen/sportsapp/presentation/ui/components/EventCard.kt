package com.kaizen.sportsapp.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.kaizen.sportsapp.presentation.R
import com.kaizen.sportsapp.presentation.model.EventModel
import com.kaizen.sportsapp.presentation.ui.theme.SportsAppTheme

@Composable
fun EventCard(
    event: EventModel,
    currentTimeSeconds: Long,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val countdown = remember(event.startTime, currentTimeSeconds) {
        val remaining = (event.startTime - currentTimeSeconds).coerceAtLeast(0)
        "%02d:%02d:%02d".format(remaining / 3600, (remaining % 3600) / 60, remaining % 60)
    }

    Column(
        modifier = modifier
            .width(100.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = countdown,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (event.isFavorite) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = stringResource(if (event.isFavorite) R.string.cd_remove_from_favorites else R.string.cd_add_to_favorites),
                tint = if (event.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = event.competitor1,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = event.competitor2,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun EventCardPreview() {
    SportsAppTheme {
        EventCard(
            event = EventModel(
                id = "1",
                sportId = "FOOT",
                competitor1 = "Aris",
                competitor2 = "Olympiakos",
                startTime = 1_700_004_000L,
                isFavorite = false
            ),
            currentTimeSeconds = 1_700_000_000L,
            onFavoriteClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun EventCardFavoritePreview() {
    SportsAppTheme {
        EventCard(
            event = EventModel(
                id = "2",
                sportId = "BASK",
                competitor1 = "Manchester United",
                competitor2 = "Chelsea",
                startTime = 1_700_007_200L,
                isFavorite = true
            ),
            currentTimeSeconds = 1_700_000_000L,
            onFavoriteClick = {}
        )
    }
}
