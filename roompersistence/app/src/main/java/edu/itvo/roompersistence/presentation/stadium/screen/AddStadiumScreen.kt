package edu.itvo.roompersistence.presentation.stadium.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.itvo.roompersistence.presentation.stadium.event.AddStadiumEvent
import edu.itvo.roompersistence.presentation.stadium.event.AddStadiumUiEvent
import edu.itvo.roompersistence.presentation.stadium.viewmodel.AddStadiumViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStadiumScreen(
    stadiumId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: AddStadiumViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    /*
    =========================================
    LOAD FOR EDIT MODE
    =========================================
     */

    LaunchedEffect(stadiumId) {
        stadiumId?.let {
            viewModel.loadStadium(it)
        }
    }

    /*
    =========================================
    HANDLE UI EVENTS
    =========================================
     */

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AddStadiumUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                    if (event.navigateBack) {
                        onNavigateBack()
                    }
                }
            }
        }
    }

    Scaffold(

        topBar = {

            CenterAlignedTopAppBar(

                title = {
                    Text(
                        if (stadiumId == null) "add stadium" else "edit stadium"
                    )
                },

                navigationIcon = {

                    IconButton(onClick = onNavigateBack) {

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },

        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            /*
            =========================================
            NAME
            =========================================
             */

            OutlinedTextField(
                value = uiState.name,
                onValueChange = {
                    viewModel.onEvent(AddStadiumEvent.OnNameChanged(it))
                },
                label = { Text("Stadium Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            /*
            =========================================
            CITY
            =========================================
             */

            OutlinedTextField(
                value = uiState.city,
                onValueChange = {
                    viewModel.onEvent(AddStadiumEvent.OnCityChanged(it))
                },
                label = { Text("City") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            /*
            =========================================
            COUNTRY
            =========================================
             */

            OutlinedTextField(
                value = uiState.country,
                onValueChange = {
                    viewModel.onEvent(AddStadiumEvent.OnCountryChanged(it))
                },
                label = { Text("Country") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            /*
            =========================================
            CAPACITY
            =========================================
             */

            OutlinedTextField(
                value = uiState.capacity,
                onValueChange = {
                    viewModel.onEvent(AddStadiumEvent.OnCapacityChanged(it))
                },
                label = { Text("Capacity") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            /*
            =========================================
            YEAR BUILT
            =========================================
             */

            OutlinedTextField(
                value = uiState.yearBuilt,
                onValueChange = {
                    viewModel.onEvent(AddStadiumEvent.OnYearBuiltChanged(it))
                },
                label = { Text("Year Built") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            /*
            =========================================
            SURFACE
            =========================================
             */

            OutlinedTextField(
                value = uiState.surface,
                onValueChange = {
                    viewModel.onEvent(AddStadiumEvent.OnSurfaceChanged(it))
                },
                label = { Text("Surface (e.g. Natural Grass, Artificial Turf)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            /*
            =========================================
            SAVE BUTTON
            =========================================
             */

            Button(
                onClick = {
                    viewModel.onEvent(AddStadiumEvent.SaveStadium)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Save Stadium")
            }
        }
    }
}
