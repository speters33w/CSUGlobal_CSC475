package edu.csuglobal.csc475.chefssliderule

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider

import edu.csuglobal.csc475.chefssliderule.models.ConversionModel
import edu.csuglobal.csc475.chefssliderule.services.ConversionService
import edu.csuglobal.csc475.chefssliderule.ui.components.CustomKeyboard
import edu.csuglobal.csc475.chefssliderule.ui.screens.ConversionInputField
import edu.csuglobal.csc475.chefssliderule.utils.UnitFormatter
import edu.csuglobal.csc475.chefssliderule.viewmodels.ConversionViewModel



class ConversionViewModelFactory(private val conversionService: ConversionService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConversionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConversionViewModel(conversionService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// Emojis for Top Bar
fun getConversionTypeEmoji(conversionType: ConversionService.ConversionType): String {
    return when (conversionType) {
        ConversionService.ConversionType.VOLUME -> "🧪"
        ConversionService.ConversionType.WEIGHT -> "⚖️"
        ConversionService.ConversionType.TEMPERATURE -> "🌡️"
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChefsSlideRuleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChefsSlideRuleApp()
                }
            }
        }
    }
}

@Composable
fun ChefsSlideRuleTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(),
        content = content
    )
}


@Composable
fun ChefsSlideRuleApp() {
    val conversionService = remember { ConversionService() }
    val viewModel: ConversionViewModel = viewModel(
        factory = ConversionViewModelFactory(conversionService)
    )
    // Observe ViewModel state
    val currentConversion by viewModel.currentConversion.observeAsState(
        ConversionModel(
            value = 0.0,
            fromUnit = "",
            toUnit = "",
            conversionType = ConversionService.ConversionType.VOLUME
        )
    )
    val conversionResult by viewModel.conversionResult.observeAsState("")
    val errorMessage by viewModel.errorMessage.observeAsState("")

    var inputValue by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var showCustomKeyboard by remember { mutableStateOf(true) }

    // Move LaunchedEffect inside the composable
    LaunchedEffect(Unit) {
        focusManager.moveFocus(FocusDirection.Down)
    }

    // Get available units based on conversion type
    val availableUnits = when (currentConversion.conversionType) {
        ConversionService.ConversionType.VOLUME -> {
            listOf(
                ConversionService.VolumeUnit.TEASPOON.name,
                ConversionService.VolumeUnit.TABLESPOON.name,
                ConversionService.VolumeUnit.CUP.name,
                ConversionService.VolumeUnit.FLUID_OUNCE.name,
                ConversionService.VolumeUnit.PINT.name,
                ConversionService.VolumeUnit.QUART.name,
                ConversionService.VolumeUnit.GALLON.name,
                ConversionService.VolumeUnit.MILLILITER.name,
                ConversionService.VolumeUnit.LITER.name
            )
        }

        ConversionService.ConversionType.WEIGHT -> {
            listOf(
                ConversionService.WeightUnit.OUNCE.name,
                ConversionService.WeightUnit.POUND.name,
                ConversionService.WeightUnit.GRAM.name,
                ConversionService.WeightUnit.KILOGRAM.name
            )
        }

        ConversionService.ConversionType.TEMPERATURE -> {
            listOf(
                ConversionService.TemperatureUnit.FAHRENHEIT.name,
                ConversionService.TemperatureUnit.CELSIUS.name,
                ConversionService.TemperatureUnit.KELVIN.name
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    showCustomKeyboard = false
                })
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // App Title
            Text(
                text = "Chef's Slide Rule",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Conversion Type Selection
            // Conversion Type Selection
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ConversionTypeButton(
                    conversionType = ConversionService.ConversionType.VOLUME,
                    selected = currentConversion.conversionType == ConversionService.ConversionType.VOLUME,
                    onClick = {
                        viewModel.updateConversionType(ConversionService.ConversionType.VOLUME.name)
                    }
                )
                ConversionTypeButton(
                    conversionType = ConversionService.ConversionType.WEIGHT,
                    selected = currentConversion.conversionType == ConversionService.ConversionType.WEIGHT,
                    onClick = {
                        viewModel.updateConversionType(ConversionService.ConversionType.WEIGHT.name)
                    }
                )
                ConversionTypeButton(
                    conversionType = ConversionService.ConversionType.TEMPERATURE,
                    selected = currentConversion.conversionType == ConversionService.ConversionType.TEMPERATURE,
                    onClick = {
                        viewModel.updateConversionType(ConversionService.ConversionType.TEMPERATURE.name)
                    }
                )

                // Keyboard toggle button
                IconButton(
                    onClick = { showCustomKeyboard = !showCustomKeyboard },
                    modifier = Modifier.size(48.dp)
                ) {
                    Text(
                        text = "⌨️",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 32.sp,
                            color = if (showCustomKeyboard) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                }
            }

// Value Input
            ConversionInputField(
                value = inputValue,
                onValueChange = { /* Do nothing here */ },
                label = "Enter Value",
                modifier = Modifier.fillMaxWidth(),
                onClick = { /* Remove this parameter if it's not needed */ },
                readOnly = true
            )

CustomKeyboard(
    onKeyClick = { key ->
        inputValue += key
        viewModel.updateValue(inputValue)
    },
    onBackspace = {
        if (inputValue.isNotEmpty()) {
            inputValue = inputValue.dropLast(1)
            viewModel.updateValue(inputValue)
        }
    },
    onClear = {
        inputValue = ""
        viewModel.updateValue("0")
    },
    onDone = {
        showCustomKeyboard = false
        if (inputValue.isNotEmpty()) {
            viewModel.updateValue(inputValue)
        }
    }
)

            // Unit Selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // From Unit Dropdown
                UnitDropdown(
                    label = "From",
                    selectedUnit = currentConversion.fromUnit,
                    availableUnits = availableUnits,
                    onUnitSelected = { viewModel.updateFromUnit(it) },
                    modifier = Modifier.weight(1f)
                )

                // Swap Button
                IconButton(
                    onClick = { viewModel.swapUnits() },
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Swap Units"
                    )
                }

                // To Unit Dropdown
                UnitDropdown(
                    label = "To",
                    selectedUnit = currentConversion.toUnit,
                    availableUnits = availableUnits,
                    onUnitSelected = { viewModel.updateToUnit(it) },
                    modifier = Modifier.weight(1f)
                )
            }

            // Result Display
            // Result Display
            if (conversionResult.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = "${currentConversion.value} ${
                            UnitFormatter.getUnitAbbreviation(
                                currentConversion.fromUnit
                            )
                        } = $conversionResult ${UnitFormatter.getUnitAbbreviation(currentConversion.toUnit)}",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 20.sp
                    )
                }
            }

            // Error Display
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

    @Composable
    fun ConversionTypeButton(
        conversionType: ConversionService.ConversionType,
        selected: Boolean,
        onClick: () -> Unit
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(48.dp)
        ) {
            Text(
                text = getConversionTypeEmoji(conversionType),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 32.sp,
                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UnitDropdown(
        label: String,
        selectedUnit: String,
        availableUnits: List<String>,
        onUnitSelected: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = modifier
        ) {
            OutlinedTextField(
                value = UnitFormatter.formatUnitName(selectedUnit),
                onValueChange = {},
                readOnly = true,
                label = { Text(label) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                availableUnits.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(UnitFormatter.formatUnitName(unit)) },
                        onClick = {
                            onUnitSelected(unit)
                            expanded = false
                        }
                    )
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        ChefsSlideRuleTheme {
            ChefsSlideRuleApp()
        }
    }


