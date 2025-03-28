package edu.csuglobal.csc475.chefssliderule.utils

import java.util.Locale

object UnitFormatter {
    fun formatUnitName(unit: String): String {
        return unit.lowercase(Locale.ROOT)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }

    fun getUnitAbbreviation(unit: String): String {
        return when (unit.uppercase(Locale.ROOT)) {
            "TEASPOON" -> "tsp"
            "TABLESPOON" -> "Tbsp"
            "CUP" -> "cup"
            "FLUID_OUNCE" -> "fl oz"
            "PINT" -> "pt"
            "QUART" -> "qt"
            "GALLON" -> "gal"
            "MILLILITER" -> "mL"
            "LITER" -> "L"
            "OUNCE" -> "oz"
            "POUND" -> "lb"
            "GRAM" -> "g"
            "KILOGRAM" -> "kg"
            "FAHRENHEIT" -> "°F"
            "CELSIUS" -> "°C"
            "KELVIN" -> "K"
            else -> unit
        }
    }
}