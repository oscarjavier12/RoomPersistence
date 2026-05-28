package edu.itvo.roompersistence.presentation.stadium.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.itvo.roompersistence.presentation.stadium.components.StadiumItem
import edu.itvo.roompersistence.presentation.stadium.event.StadiumListEvent
import edu.itvo.roompersistence.presentation.stadium.viewmodel.StadiumListViewModel

@Composable
fun StadiumListScreen(
    onNavigateToAddStadium: () -> Unit,
    onNavigateToEditStadium: (Long) -> Unit,
    viewModel: StadiumListViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(

        floatingActionButton = {

            FloatingActionButton(
                onClick = onNavigateToAddStadium
            ) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }

    ) { innerPadding ->

        if (uiState.stadiums.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "No stadiums yet.\nTap + to add one.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = uiState.stadiums,
                    key = { stadium -> stadium.id }
                ) { stadium ->

                    StadiumItem(
                        stadium = stadium,
                        onEditClick = {
                            onNavigateToEditStadium(stadium.id)
                        },
                        onDeleteClick = {
                            viewModel.onEvent(
                                StadiumListEvent.DeleteStadium(stadium)
                            )
                        }
                    )
                }
            }
        }
    }
}