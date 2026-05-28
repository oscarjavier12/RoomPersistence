package edu.itvo.roompersistence.presentation.player.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Stadium
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.itvo.roompersistence.presentation.player.component.PlayerItem
import edu.itvo.roompersistence.presentation.player.event.PlayerListEvent
import edu.itvo.roompersistence.presentation.player.viewmodel.PlayerListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerListScreen(
    onNavigateToAddPlayer: () -> Unit,
    onNavigateToEditPlayer: (Long) -> Unit,
    onNavigateToStadiums: () -> Unit,
    viewModel: PlayerListViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(

        topBar = {

            CenterAlignedTopAppBar(

                title = { Text("Players") },

                actions = {

                    IconButton(onClick = onNavigateToStadiums) {

                        Icon(
                            imageVector = Icons.Default.Stadium,
                            contentDescription = "Stadiums"
                        )
                    }
                }
            )
        },

        floatingActionButton = {

            FloatingActionButton(onClick = onNavigateToAddPlayer) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }

    ) { innerPadding ->

        if (uiState.players.isEmpty()) {

            EmptyPlayerScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = uiState.players,
                    key = { player -> player.id }
                ) { player ->

                    PlayerItem(
                        player = player,
                        onEditClick = {
                            onNavigateToEditPlayer(player.id)
                        },
                        onDeleteClick = {
                            viewModel.onEvent(
                                PlayerListEvent.DeletePlayer(player)
                            )
                        }
                    )
                }
            }
        }
    }
}