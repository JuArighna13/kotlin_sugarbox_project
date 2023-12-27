package dto

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DailySummaryTest {
    private val objectMapper: ObjectMapper = jacksonObjectMapper().registerKotlinModule()

    @Test
    fun `serialize and deserialize DailySummary should match`() {
        // Arrange
        val dailySummary = DailySummary(userId = 1, date = "2023-01-01", post = 2, likeReceived = 1)

        // Act
        val jsonString = objectMapper.writeValueAsString(dailySummary)
        val deserialized = objectMapper.readValue<DailySummary>(jsonString)

        // Assert
        assertEquals(dailySummary, deserialized)
    }
}
