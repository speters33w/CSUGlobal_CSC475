package edu.csuglobal.csc475.chefssliderule.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.csuglobal.csc475.chefssliderule.models.ConversionModel
import edu.csuglobal.csc475.chefssliderule.services.ConversionService

/**
 * ViewModel for handling conversion operations and UI state
 */
class ConversionViewModel(private val conversionService: ConversionService) : ViewModel() {
    
    // LiveData for the current conversion result
    private val _conversionResult = MutableLiveData<String>()
    val conversionResult: LiveData<String> = _conversionResult
    
    // LiveData for the current conversion model
    private val _currentConversion = MutableLiveData<ConversionModel>()
    val currentConversion: LiveData<ConversionModel> = _currentConversion
    
    // LiveData for error messages
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    // Available units for each conversion type
    private val volumeUnits = conversionService.getVolumeUnits()
    private val weightUnits = conversionService.getWeightUnits()
    private val temperatureUnits = conversionService.getTemperatureUnits()
    
    init {
        // Initialize with default conversion model
        _currentConversion.value = ConversionModel(
            value = 0.0,
            fromUnit = conversionService.getVolumeUnits()[0].name,
            toUnit = conversionService.getVolumeUnits()[1].name,
            conversionType = ConversionService.ConversionType.VOLUME
        )
    }
    
    /**
     * Updates the current conversion value
     * 
     * @param value The new value to convert
     */
    fun updateValue(value: Double) {
        val current = _currentConversion.value ?: return
        _currentConversion.value = current.copy(value = value)
        performConversion()
    }
    
    /**
     * Updates the source unit for conversion
     * 
     * @param unit The new source unit
     */
    fun updateFromUnit(unit: String) {
        val current = _currentConversion.value ?: return
        _currentConversion.value = current.copy(fromUnit = unit)
        performConversion()
    }
    
    /**
     * Updates the target unit for conversion
     * 
     * @param unit The new target unit
     */
    fun updateToUnit(unit: String) {
        val current = _currentConversion.value ?: return
        _currentConversion.value = current.copy(toUnit = unit)
        performConversion()
    }
    
    /**
     * Updates the type of conversion (volume, weight, temperature)
     * 
     * @param type The new conversion type
     */
    fun updateConversionType(type: String) {
        val current = _currentConversion.value ?: return
        
        // Convert string to enum
        val conversionType = ConversionService.ConversionType.valueOf(type)
        
        // Set default units based on the conversion type
        val (fromUnit, toUnit) = when (conversionType) {
            ConversionService.ConversionType.VOLUME -> Pair(volumeUnits[0].name, volumeUnits[1].name)
            ConversionService.ConversionType.WEIGHT -> Pair(weightUnits[0].name, weightUnits[1].name)
            ConversionService.ConversionType.TEMPERATURE -> Pair(temperatureUnits[0].name, temperatureUnits[1].name)
        }
        
        _currentConversion.value = current.copy(
            conversionType = conversionType,
            fromUnit = fromUnit,
            toUnit = toUnit
        )
        performConversion()
    }
    
    /**
     * Performs the conversion based on the current conversion model
     */
    private fun performConversion() {
        val model = _currentConversion.value ?: return
        
        try {
            val result = when (model.conversionType) {
                ConversionService.ConversionType.VOLUME -> {
                    val fromUnit = ConversionService.VolumeUnit.valueOf(model.fromUnit)
                    val toUnit = ConversionService.VolumeUnit.valueOf(model.toUnit)
                    conversionService.convertVolume(model.value, fromUnit, toUnit)
                }
                ConversionService.ConversionType.WEIGHT -> {
                    val fromUnit = ConversionService.WeightUnit.valueOf(model.fromUnit)
                    val toUnit = ConversionService.WeightUnit.valueOf(model.toUnit)
                    conversionService.convertWeight(model.value, fromUnit, toUnit)
                }
                ConversionService.ConversionType.TEMPERATURE -> {
                    val fromUnit = ConversionService.TemperatureUnit.valueOf(model.fromUnit)
                    val toUnit = ConversionService.TemperatureUnit.valueOf(model.toUnit)
                    conversionService.convertTemperature(model.value, fromUnit, toUnit)
                }
            }
            
            _conversionResult.value = conversionService.formatResult(result)
            _errorMessage.value = "" // Changed from null to empty string
        } catch (e: Exception) {
            _errorMessage.value = "Error performing conversion: ${e.message}"
            _conversionResult.value = ""
        }
    }
    
    /**
     * Swaps the from and to units
     */
    fun swapUnits() {
        val current = _currentConversion.value ?: return
        _currentConversion.value = current.copy(
            fromUnit = current.toUnit,
            toUnit = current.fromUnit
        )
        performConversion()
    }
}