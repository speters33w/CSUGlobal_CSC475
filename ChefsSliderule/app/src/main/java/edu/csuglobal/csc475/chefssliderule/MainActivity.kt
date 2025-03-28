package edu.csuglobal.csc475.chefssliderule

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import edu.csuglobal.csc475.chefssliderule.models.ConversionModel
import edu.csuglobal.csc475.chefssliderule.services.ConversionService
import edu.csuglobal.csc475.chefssliderule.utils.UnitFormatter
import edu.csuglobal.csc475.chefssliderule.viewmodels.ConversionViewModel




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
    val currentConversion by viewModel.currentConversion.observeAsState(ConversionModel(
        value = 0.0,
        fromUnit = "",
        toUnit = "",
        conversionType = ConversionService.ConversionType.VOLUME
    ))
    val conversionResult by viewModel.conversionResult.observeAsState("")
    val errorMessage by viewModel.errorMessage.observeAsState("")
    
    var inputValue by remember { mutableStateOf(currentConversion.value.toString()) }
    
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    
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
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                })
            },
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Title
        Text(
            text = "Chef's Slide Rule",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // Conversion Type Selection
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ConversionTypeButton(
                    text = "Volume",
                    selected = currentConversion.conversionType == ConversionService.ConversionType.VOLUME,
                    onClick = { viewModel.updateConversionType(ConversionService.ConversionType.VOLUME.name) }
                )
                ConversionTypeButton(
                    text = "Weight",
                    selected = currentConversion.conversionType == ConversionService.ConversionType.WEIGHT,
                    onClick = { viewModel.updateConversionType(ConversionService.ConversionType.WEIGHT.name) }
                )
                ConversionTypeButton(
                    text = "Temperature",
                    selected = currentConversion.conversionType == ConversionService.ConversionType.TEMPERATURE,
                    onClick = { viewModel.updateConversionType(ConversionService.ConversionType.TEMPERATURE.name) }
                )
            }
        }
        
        // Value Input
        var textFieldValue by remember { mutableStateOf(TextFieldValue(text = inputValue)) }
        
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                textFieldValue = newValue
                inputValue = newValue.text
                newValue.text.toDoubleOrNull()?.let { value -> viewModel.updateValue(value) }
            },
            label = { Text("Enter Value") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        textFieldValue = textFieldValue.copy(selection = TextRange(0, textFieldValue.text.length))
                        keyboardController?.show()
                    }
                },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
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
                    imageVector = Icons.Default.SwapVert,
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
                    text = "${currentConversion.value} ${UnitFormatter.getUnitAbbreviation(currentConversion.fromUnit)} = $conversionResult ${UnitFormatter.getUnitAbbreviation(currentConversion.toUnit)}",
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

@Composable
fun ConversionTypeButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Text(text)
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

class ConversionViewModelFactory(private val conversionService: ConversionService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConversionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConversionViewModel(conversionService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}