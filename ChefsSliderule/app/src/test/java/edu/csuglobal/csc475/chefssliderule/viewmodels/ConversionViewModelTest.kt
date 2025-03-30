package edu.csuglobal.csc475.chefssliderule.viewmodels

import androidx.lifecycle.Observer
import edu.csuglobal.csc475.chefssliderule.models.ConversionModel
import edu.csuglobal.csc475.chefssliderule.services.ConversionService
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
//import org.junit.jupiter.api.extension.InstantTaskExecutorRuleForJUnit5

@ExtendWith(InstantTaskExecutorRuleForJUnit5::class)
class ConversionViewModelTest {

    private lateinit var conversionService: ConversionService
    private lateinit var viewModel: ConversionViewModel

    private val observer = mockk<Observer<Any>>(relaxed = true)

    @BeforeEach
    fun setup() {
        conversionService = mockk(relaxed = true)
        every { conversionService.getVolumeUnits() } returns listOf(
            ConversionService.VolumeUnit.CUP,
            ConversionService.VolumeUnit.MILLILITER
        )
        every { conversionService.getWeightUnits() } returns listOf(
            ConversionService.WeightUnit.OUNCE,
            ConversionService.WeightUnit.GRAM
        )
        every { conversionService.getTemperatureUnits() } returns listOf(
            ConversionService.TemperatureUnit.FAHRENHEIT,
            ConversionService.TemperatureUnit.CELSIUS
        )
        viewModel = ConversionViewModel(conversionService)
    }

    @Test
    fun `test updateValue updates currentConversion and calls performConversion`() {
        // Arrange
        val testValue = "2.5"
        val parsedValue = 2.5
        val initialConversionModel = viewModel.currentConversion.value!!

        // Spy on the performConversion method
        val viewModelSpy = spyk(viewModel, recordPrivateCalls = true)
        coEvery { viewModelSpy["performConversion"]() } returns Unit

        // Act
        viewModelSpy.updateValue(testValue)

        // Assert
        assertEquals(initialConversionModel.copy(value = parsedValue), viewModelSpy.currentConversion.value)
        verify { viewModelSpy["performConversion"]() }
    }

    @Test
    fun `test updateFromUnit updates currentConversion and calls performConversion`() {
        // Arrange
        val fromUnit = "CUP"
        val initialConversionModel = viewModel.currentConversion.value!!

        // Spy on performConversion
        val viewModelSpy = spyk(viewModel, recordPrivateCalls = true)
           coEvery { viewModelSpy["performConversion"]() } returns Unit

        // Act
        viewModelSpy.updateFromUnit(fromUnit)

        // Assert
        assertEquals(initialConversionModel.copy(fromUnit = fromUnit), viewModelSpy.currentConversion.value)
        verify { viewModelSpy["performConversion"]() }
    }

    @Test
    fun `test updateToUnit updates currentConversion and calls performConversion`() {
        // Arrange
        val toUnit = "MILLILITER"
        val initialConversionModel = viewModel.currentConversion.value!!

        // Spy on performConversion
        val viewModelSpy = spyk(viewModel, recordPrivateCalls = true)
           coEvery { viewModelSpy["performConversion"]() } returns Unit

        // Act
        viewModelSpy.updateToUnit(toUnit)

        // Assert
        assertEquals(initialConversionModel.copy(toUnit = toUnit), viewModelSpy.currentConversion.value)
        verify { viewModelSpy["performConversion"]() }
    }

    @Test
    fun `test swapUnits updates currentConversion and calls performConversion`() {
        // Arrange
        val initialModel = ConversionModel(
            value = 2.5,
            fromUnit = "CUP",
            toUnit = "MILLILITER",
            conversionType = ConversionService.ConversionType.VOLUME
        )
        viewModel.updateValue("2.5")
        viewModel.updateFromUnit("CUP")
        viewModel.updateToUnit("MILLILITER")

        // Spy on performConversion
        val viewModelSpy = spyk(viewModel, recordPrivateCalls = true)
           coEvery { viewModelSpy["performConversion"]() } returns Unit

        // Act
        viewModelSpy.swapUnits()

        // Assert
        val updatedModel = initialModel.copy(fromUnit = "MILLILITER", toUnit = "CUP")
        assertEquals(updatedModel, viewModelSpy.currentConversion.value)
        verify { viewModelSpy["performConversion"]() }
    }

    @Test
    fun `test updateConversionType updates currentConversion and calls performConversion`() {
        // Arrange
        val conversionType = ConversionService.ConversionType.WEIGHT
        every { conversionService.getWeightUnits() } returns listOf(
            ConversionService.WeightUnit.OUNCE,
            ConversionService.WeightUnit.GRAM
        )

        // Spy on performConversion
        val viewModelSpy = spyk(viewModel, recordPrivateCalls = true)
           coEvery { viewModelSpy["performConversion"]() } returns Unit

        // Act
        viewModelSpy.updateConversionType(conversionType.name)

        // Assert
        assertEquals(
            conversionType,
            viewModelSpy.currentConversion.value?.conversionType
        )
        assertEquals("OUNCE", viewModelSpy.currentConversion.value?.fromUnit)
        assertEquals("GRAM", viewModelSpy.currentConversion.value?.toUnit)
        verify { viewModelSpy["performConversion"]() }
    }

    @Test
    fun `test performConversion with successful volume conversion`() {
        // Arrange
        val model = ConversionModel(
            value = 1.0,
            fromUnit = "CUP",
            toUnit = "MILLILITER",
            conversionType = ConversionService.ConversionType.VOLUME
        )
        viewModel.updateValue("1.0")
        viewModel.updateFromUnit("CUP")
        viewModel.updateToUnit("MILLILITER")
        every { conversionService.convertVolume(1.0, ConversionService.VolumeUnit.CUP, ConversionService.VolumeUnit.MILLILITER) } returns 236.588
        every { conversionService.formatVolumeResult(236.588) } returns "236.588"

        val resultObserver = mockk<Observer<String>>(relaxed = true)
        viewModel.conversionResult.observeForever(resultObserver)

        // Act
        viewModel.performConversion()

        // Assert
        verify {
            resultObserver.onChanged("236.588")
        }
    }

//    @Test
//    fun `test performConversion sets errorMessage on exception`() {
//        // Arrange
//        val model = ConversionModel(
//            value = 1.0,
//            fromUnit = "CUP",
//            toUnit = "MILLILITER",
//            conversionType = ConversionService.ConversionType.VOLUME
//        )
//        viewModel.updateFromUnit("INVALID_UNIT")
//        viewModel.updateToUnit("MILLILITER")
//        val errorObserver = mockk<Observer<String>>(relaxed = true)
//        viewModel.errorMessage.observeForever(errorObserver)
//
//        every { conversionService.convertVolume(any(), any(), any()) } throws IllegalArgumentException("Invalid unit!")
//
//        // Act
//        viewModel.performConversion()
//
//        // Assert
//        verify {
//            errorObserver.onChanged("Error performing conversion: Invalid unit!")
//        }
//    }
}