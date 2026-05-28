package edu.itvo.roompersistence.presentation.player.screen

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import edu.itvo.roompersistence.core.createImageUri
import edu.itvo.roompersistence.domain.model.Gender
import edu.itvo.roompersistence.domain.model.Position
import edu.itvo.roompersistence.presentation.core.components.BaseCard
import edu.itvo.roompersistence.presentation.player.event.AddPlayerEvent
import edu.itvo.roompersistence.presentation.player.event.AddPlayerUiEvent
import edu.itvo.roompersistence.presentation.player.viewmodel.AddPlayerViewModel
import java.time.Instant
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlayerScreen(
    playerId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: AddPlayerViewModel = hiltViewModel()
){

    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState =
        remember {
            SnackbarHostState()
        }

    var genderExpanded by remember {
        mutableStateOf(false)
    }

    var positionExpanded by remember {
        mutableStateOf(false)
    }

    /*
    =========================================
    UI EVENTS
    =========================================
     */

    LaunchedEffect(Unit) {

        viewModel.uiEvent.collect { event ->

            when (event) {

                is AddPlayerUiEvent.ShowSnackbar -> {

                    snackbarHostState.showSnackbar(
                        message = event.message
                    )

                    if (event.navigateBack) {

                        onNavigateBack()
                    }
                }
            }
        }
    }

    /*
    =========================================
    DATE PICKER
    =========================================
     */

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    val datePickerState =
        rememberDatePickerState(
            initialDisplayMode = DisplayMode.Picker
        )

    /*
    =========================================
    TEMP CAMERA URI
    =========================================
     */

    var cameraImageUri by remember {
        mutableStateOf<Uri>(
            createImageUri(context)
        )
    }

    /*
    =========================================
    GALLERY PICKER
    =========================================
     */

    val imagePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->

            uri?.let {

                viewModel.onEvent(
                    AddPlayerEvent.OnPhotoSelected(
                        it.toString()
                    )
                )
            }
        }

    /*
    =========================================
    CAMERA LAUNCHER
    =========================================
     */

    val takePictureLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success ->

            if (success) {

                viewModel.onEvent(
                    AddPlayerEvent.OnPhotoSelected(
                        cameraImageUri.toString()
                    )
                )

                cameraImageUri =
                    createImageUri(context)
            }
        }

    /*
    =========================================
    CAMERA PERMISSION
    =========================================
     */

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {

                takePictureLauncher.launch(
                    cameraImageUri
                )
            }
        }

    /*
    =========================================
    DATE PICKER DIALOG
    =========================================
     */

    if (showDatePicker) {

        DatePickerDialog(

            onDismissRequest = {
                showDatePicker = false
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        val selectedDateMillis =
                            datePickerState.selectedDateMillis

                        if (selectedDateMillis != null) {

                            val localDate =
                                Instant
                                    .ofEpochMilli(selectedDateMillis)
                                    .atZone(
                                        ZoneId.systemDefault()
                                    )
                                    .toLocalDate()

                            viewModel.onEvent(
                                AddPlayerEvent.OnBirthDateChanged(
                                    localDate
                                )
                            )
                        }

                        showDatePicker = false
                    }

                ) {

                    Text("Accept")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {

                    Text("Cancel")
                }
            }

        ) {

            DatePicker(
                state = datePickerState
            )
        }
    }

    Scaffold(

        snackbarHost = {

            SnackbarHost(
                hostState = snackbarHostState
            )
        },

        topBar = {

            CenterAlignedTopAppBar(

                title = {
                    Text(

                        if (playerId == null) {
                            "Add Player"
                        } else {
                            "Edit Player"
                        }
                    )
                },

                navigationIcon = {

                    IconButton(
                        onClick = onNavigateBack
                    ) {

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = Color.Unspecified,
                    actionIconContentColor = Color.Unspecified
                )
            )
        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            BaseCard() {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),

                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    /*
                    =========================================
                    FULL NAME
                    =========================================
                     */

                    OutlinedTextField(
                        value = uiState.fullName,
                        onValueChange = {

                            viewModel.onEvent(
                                AddPlayerEvent.OnFullNameChanged(it)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Full Name")
                        },
                        singleLine = true
                    )

                    /*
                    =========================================
                    NICKNAME
                    =========================================
                     */

                    OutlinedTextField(
                        value = uiState.nickName,
                        onValueChange = {

                            viewModel.onEvent(
                                AddPlayerEvent.OnNickNameChanged(it)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Nickname")
                        },
                        singleLine = true
                    )

                    /*
                    =========================================
                    NATIONALITY
                    =========================================
                     */

                    OutlinedTextField(
                        value = uiState.nationality,
                        onValueChange = {

                            viewModel.onEvent(
                                AddPlayerEvent.OnNationalityChanged(it)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Nationality")
                        },
                        singleLine = true
                    )

                    /*
                    =========================================
                    GENDER
                    =========================================
                     */

                    ExposedDropdownMenuBox(
                        expanded = genderExpanded,
                        onExpandedChange = {
                            genderExpanded = !genderExpanded
                        }
                    ) {

                        OutlinedTextField(
                            value = uiState.gender.name,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .menuAnchor(
                                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                    enabled = true
                                )
                                .fillMaxWidth(),
                            label = {
                                Text("Gender")
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = genderExpanded
                                )
                            }
                        )

                        ExposedDropdownMenu(
                            expanded = genderExpanded,
                            onDismissRequest = {
                                genderExpanded = false
                            }
                        ) {

                            Gender.entries.forEach { gender ->

                                DropdownMenuItem(
                                    text = {
                                        Text(gender.name)
                                    },
                                    onClick = {

                                        viewModel.onEvent(
                                            AddPlayerEvent.OnGenderChanged(
                                                gender
                                            )
                                        )

                                        genderExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    /*
                    =========================================
                    POSITION
                    =========================================
                     */

                    ExposedDropdownMenuBox(
                        expanded = positionExpanded,
                        onExpandedChange = {
                            positionExpanded = !positionExpanded
                        }
                    ) {

                        OutlinedTextField(
                            value = uiState.position.displayName,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .menuAnchor(
                                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                    enabled = true
                                )
                                .fillMaxWidth(),
                            label = {
                                Text("Position")
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = positionExpanded
                                )
                            }
                        )

                        ExposedDropdownMenu(
                            expanded = positionExpanded,
                            onDismissRequest = {
                                positionExpanded = false
                            }
                        ) {

                            Position.entries.forEach { position ->

                                DropdownMenuItem(
                                    text = {
                                        Text(position.displayName)
                                    },
                                    onClick = {

                                        viewModel.onEvent(
                                            AddPlayerEvent.OnPositionChanged(
                                                position
                                            )
                                        )

                                        positionExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    /*
                    =========================================
                    BIRTH DATE
                    =========================================
                     */

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                                showDatePicker = true
                            }
                    ) {

                        OutlinedTextField(
                            value = uiState.birthDate?.toString() ?: "",
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false,
                            label = {
                                Text("Birth Date")
                            }
                        )
                    }

                    /*
                    =========================================
                    PHOTO SECTION
                    =========================================
                     */

                    Button(
                        onClick = {

                            imagePickerLauncher.launch(

                                PickVisualMediaRequest(
                                    ActivityResultContracts
                                        .PickVisualMedia
                                        .ImageOnly
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = null
                        )

                        Text(" Select From Gallery")
                    }

                    Button(
                        onClick = {

                            cameraPermissionLauncher.launch(
                                Manifest.permission.CAMERA
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = null
                        )

                        Text(" Take Photo")
                    }

                    HorizontalDivider()

                    /*
                    =========================================
                    PHOTO PREVIEW
                    =========================================
                     */

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        if (uiState.photoUri != null) {

                            AsyncImage(
                                model = uiState.photoUri,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp),
                                contentScale = ContentScale.Crop
                            )

                        } else {

                            Text(
                                text = "No photo selected",
                                modifier = Modifier.padding(24.dp)
                            )
                        }
                    }

                    /*
                    =========================================
                    SAVE BUTTON
                    =========================================
                     */

                    Button(
                        onClick = {

                            viewModel.onEvent(
                                AddPlayerEvent.SavePlayer
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {

                        Text(

                            if (playerId == null) {
                                "Save Player"
                            } else {
                                "Update Player"
                            }
                        )
                    }
                }
            }
        }
    }
}