package edu.csuglobal.csc475.chefssliderule.models

import edu.csuglobal.csc475.chefssliderule.services.ConversionService

data class ConversionModel(
    val value: Double,
    val fromUnit: String,
    val toUnit: String,
    val result: Double = 0.0,
    val conversionType: ConversionService.ConversionType = ConversionService.ConversionType.VOLUME
)

