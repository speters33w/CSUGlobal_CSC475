package edu.csuglobal.csc475.chefssliderule.services

import kotlin.math.roundToInt

/**
 * Service class for handling unit conversions
 */
class ConversionService {

    /**
     * Enum representing different types of conversions
     */
    enum class ConversionType {
        VOLUME,
        WEIGHT,
        TEMPERATURE
    }

    /**
     * Volume units for conversion
     */
    enum class VolumeUnit(val factor: Double) {
        TEASPOON(4.929),
        TABLESPOON(14.787),
        FLUID_OUNCE(29.574),
        CUP(236.588),
        PINT(473.176),
        QUART(946.353),
        GALLON(3785.41),
        MILLILITER(1.0),
        LITER(1000.0)
    }

    /**
     * Weight units for conversion
     */
    enum class WeightUnit(val factor: Double) {
        OUNCE(28.3495),
        POUND(453.592),
        GRAM(1.0),
        KILOGRAM(1000.0)
    }

    /**
     * Temperature units for conversion
     */
    enum class TemperatureUnit {
        FAHRENHEIT,
        CELSIUS,
        KELVIN
    }

    /**
     * Get all available volume units
     */
    /**
     * Get all available volume units
     */
    fun getVolumeUnits(): List<VolumeUnit> = VolumeUnit.entries.toList()
    
    /**
     * Get all available weight units
     */
    fun getWeightUnits(): List<WeightUnit> = WeightUnit.entries.toList()
    
    /**
     * Get all available temperature units
     */
    fun getTemperatureUnits(): List<TemperatureUnit> = TemperatureUnit.entries.toList()

    /**
     * Convert volume from one unit to another
     */
    fun convertVolume(value: Double, fromUnit: VolumeUnit, toUnit: VolumeUnit): Double {
        if (fromUnit == toUnit) return value
        return (value * fromUnit.factor / toUnit.factor).round(4)
    }

    /**
     * Convert weight from one unit to another
     */
    fun convertWeight(value: Double, fromUnit: WeightUnit, toUnit: WeightUnit): Double {
        if (fromUnit == toUnit) return value
        return (value * fromUnit.factor / toUnit.factor).round(4)
    }

    /**
     * Convert temperature from one unit to another
     */
    fun convertTemperature(value: Double, fromUnit: TemperatureUnit, toUnit: TemperatureUnit): Double {
        if (fromUnit == toUnit) return value
        
        // Convert to Kelvin first
        val kelvin = when (fromUnit) {
            TemperatureUnit.CELSIUS -> value + 273.15
            TemperatureUnit.FAHRENHEIT -> (value - 32) * 5/9 + 273.15
            TemperatureUnit.KELVIN -> value
        }
        
        // Convert from Kelvin to target unit
        return when (toUnit) {
            TemperatureUnit.CELSIUS -> (kelvin - 273.15).round(2)
            TemperatureUnit.FAHRENHEIT -> ((kelvin - 273.15) * 9/5 + 32).round(2)
            TemperatureUnit.KELVIN -> kelvin.round(2)
        }
    }

    /**
     * Format the result for display
     */
    fun formatResult(value: Double): String {
        return if (value == value.toInt().toDouble()) {
            value.toInt().toString()
        } else {
            value.toString()
        }
    }
fun formatVolumeResult(value: Double): String {
    val integerPart = value.toInt()
    val fractionalPart = value - integerPart

    return when {
        fractionalPart < 0.063 -> integerPart.toString()
        fractionalPart < 0.188 -> if (integerPart == 0) "\u215B" else "$integerPart\u215B"  // ⅛
        fractionalPart < 0.292 -> if (integerPart == 0) "\u00BC" else "$integerPart\u00BC"  // ¼
        fractionalPart < 0.354 -> if (integerPart == 0) "\u2153" else "$integerPart\u2153"  // ⅓
        fractionalPart < 0.438 -> if (integerPart == 0) "\u215C" else "$integerPart\u215C"  // ⅜
        fractionalPart < 0.563 -> if (integerPart == 0) "\u00BD" else "$integerPart\u00BD"  // ½
        fractionalPart < 0.646 -> if (integerPart == 0) "\u215D" else "$integerPart\u215D"  // ⅝
        fractionalPart < 0.709 -> if (integerPart == 0) "\u2154" else "$integerPart\u2154"  // ⅔
        fractionalPart < 0.813 -> if (integerPart == 0) "\u00BE" else "$integerPart\u00BE"  // ¾
        fractionalPart < 0.948 -> if (integerPart == 0) "\u215E" else "$integerPart\u215E"  // ⅞
        else -> (integerPart + 1).toString()
    }
}

    /**
     * Round a double to a specific number of decimal places
     */
    private fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return (this * multiplier).roundToInt() / multiplier
    }
}