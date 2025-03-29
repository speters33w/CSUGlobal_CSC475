package edu.csuglobal.csc475.chefssliderule

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import edu.csuglobal.csc475.chefssliderule.services.ConversionService
import edu.csuglobal.csc475.chefssliderule.viewmodels.ConversionViewModel
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import io.mockk.*
import org.robolectric.annotation.Config

class InstantTaskExecutorRuleForJUnit5 : AfterEachCallback, BeforeEachCallback {
    override fun beforeEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) = runnable.run()
            override fun postToMainThread(runnable: Runnable) = runnable.run()
            override fun isMainThread(): Boolean = true
        })
    }

    override fun afterEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }
}

@ExtendWith(InstantTaskExecutorRuleForJUnit5::class)
@Config(manifest = Config.NONE)
class ConversionTests {

    private lateinit var conversionService: ConversionService
    private lateinit var conversionViewModel: ConversionViewModel

    @BeforeEach
    fun setup() {
        // Create a mock of the ConversionService class using MockK
        conversionService = mockk(relaxed = true)

        // Mock the necessary methods of ConversionService
        every { conversionService.getVolumeUnits() } returns listOf(
            ConversionService.VolumeUnit.CUP,
            ConversionService.VolumeUnit.MILLILITER
        )
        every { conversionService.getWeightUnits() } returns listOf(
            ConversionService.WeightUnit.GRAM,
            ConversionService.WeightUnit.OUNCE
        )
        every { conversionService.getTemperatureUnits() } returns listOf(
            ConversionService.TemperatureUnit.CELSIUS,
            ConversionService.TemperatureUnit.FAHRENHEIT
        )

        conversionViewModel = ConversionViewModel(conversionService)
    }

    @Test
    fun testCupToMilliliterConversion() {
        // Given
        every { conversionService.convertVolume(1.0, ConversionService.VolumeUnit.CUP, ConversionService.VolumeUnit.MILLILITER) } returns 236.588
        every { conversionService.formatResult(236.588) } returns "236.59 ml"
    
        // When
        conversionViewModel.updateValue("1.0")
        conversionViewModel.updateFromUnit("CUP")
        conversionViewModel.updateToUnit("MILLILITER")
        conversionViewModel.updateConversionType("VOLUME")
    
        // Then
        // Assert that the conversion result is correct
        assertEquals("236.59 ml", conversionViewModel.conversionResult.value)
    
        // Assert that the current conversion model is updated correctly
        val currentConversion = conversionViewModel.currentConversion.value
        assertNotNull(currentConversion)
        assertEquals(1.0, currentConversion?.value)
        assertEquals("CUP", currentConversion?.fromUnit)
        assertEquals("MILLILITER", currentConversion?.toUnit)
        assertEquals(ConversionService.ConversionType.VOLUME, currentConversion?.conversionType)
    
        // Verify that the conversion method was called with the correct parameters
        verify { conversionService.convertVolume(1.0, ConversionService.VolumeUnit.CUP, ConversionService.VolumeUnit.MILLILITER) }
    }

    @Test
    fun testVolumeConversion() {
        // Given
        every { conversionService.convertVolume(1.0, ConversionService.VolumeUnit.CUP, ConversionService.VolumeUnit.MILLILITER) } returns 236.588
        every { conversionService.formatResult(236.588) } returns "236.59 ml"

        // When
        conversionViewModel.updateValue("1.0")
        conversionViewModel.updateFromUnit("CUP")
        conversionViewModel.updateToUnit("MILLILITER")
        conversionViewModel.updateConversionType("VOLUME")

        // Then
        // Assert that the conversion result is correct
        assertEquals("236.59 ml", conversionViewModel.conversionResult.value)

        // Assert that the current conversion model is updated correctly
        val currentConversion = conversionViewModel.currentConversion.value
        assertNotNull(currentConversion)
        assertEquals(1.0, currentConversion?.value)
        assertEquals("CUP", currentConversion?.fromUnit)
        assertEquals("MILLILITER", currentConversion?.toUnit)
        assertEquals(ConversionService.ConversionType.VOLUME, currentConversion?.conversionType)

        // Verify that the conversion method was called with the correct parameters
        verify { conversionService.convertVolume(1.0, ConversionService.VolumeUnit.CUP, ConversionService.VolumeUnit.MILLILITER) }
    }

    // TODO add more test methods
}