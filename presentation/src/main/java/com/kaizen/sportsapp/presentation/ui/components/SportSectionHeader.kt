package com.kaizen.sportsapp.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.kaizen.sportsapp.presentation.R
import com.kaizen.sportsapp.presentation.model.SportModel
import com.kaizen.sportsapp.presentation.ui.theme.SportsAppTheme

@Composable
fun SportSectionHeader(
    sport: SportModel,
    onFavoritesFilterToggle: () -> Unit,
    onExpandToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = sport.name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onFavoritesFilterToggle) {
            Icon(
                imageVector = if (sport.showFavoritesOnly) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = stringResource(if (sport.showFavoritesOnly) R.string.cd_show_all_events else R.string.cd_show_favorites_only),
                tint = if (sport.showFavoritesOnly) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
        IconButton(onClick = onExpandToggle) {
            Icon(
                imageVector = if (sport.isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = stringResource(if (sport.isExpanded) R.string.cd_collapse else R.string.cd_expand),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun SportSectionHeaderExpandedPreview() {
    SportsAppTheme {
        SportSectionHeader(
            sport = SportModel(
                id = "FOOT",
                name = "Football",
                events = emptyList(),
                isExpanded = true,
                showFavoritesOnly = false
            ),
            onFavoritesFilterToggle = {},
            onExpandToggle = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun SportSectionHeaderCollapsedFavoritesPreview() {
    SportsAppTheme {
        SportSectionHeader(
            sport = SportModel(
                id = "BASK",
                name = "Basketball",
                events = emptyList(),
                isExpanded = false,
                showFavoritesOnly = true
            ),
            onFavoritesFilterToggle = {},
            onExpandToggle = {}
        )
    }
}
