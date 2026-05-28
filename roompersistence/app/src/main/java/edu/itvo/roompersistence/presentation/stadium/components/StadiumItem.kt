package edu.itvo.roompersistence.presentation.stadium.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import edu.itvo.roompersistence.domain.model.Stadium
import edu.itvo.roompersistence.presentation.core.components.BaseCard

@Composable
fun StadiumItem(
    stadium: Stadium,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    BaseCard {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            /*
            =========================================
            STADIUM INFO
            =========================================
             */

            Text(
                text = stadium.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        text = "${stadium.city}, ${stadium.country}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Capacity: ${"%,d".format(stadium.capacity)}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = "Built: ${stadium.yearBuilt}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = "Surface: ${stadium.surface}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider()

            /*
            =========================================
            ACTIONS
            =========================================
             */

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                TextButton(onClick = onEditClick) {
                    Text("Edit")
                }

                TextButton(onClick = onDeleteClick) {
                    Text("Delete")
                }
            }
        }
    }
}
