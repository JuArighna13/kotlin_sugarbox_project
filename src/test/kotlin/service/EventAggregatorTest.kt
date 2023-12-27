package service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import dto.DailySummary
import dto.UserEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files

internal class EventAggregatorTest {
    private lateinit var objectMapper: ObjectMapper
    private lateinit var eventAggregator: EventAggregator
    private lateinit var tempInputFile: File
    private lateinit var tempOutputFile: File

    @BeforeEach
    fun setUp() {
        objectMapper = jacksonObjectMapper().registerKotlinModule()
        eventAggregator = EventAggregator()

        // Create temporary input and output files
        tempInputFile = createTempFile("testInput", ".json")
        tempOutputFile = createTempFile("testOutput", ".json")
    }

    @Test
    fun `aggregateEvents should correctly aggregate events`() {
        // Arrange
        val events = listOf(
            UserEvent(1, "post", 1672444800),
            UserEvent(1, "likeReceived", 1672444801),
            UserEvent(2, "comment", 1672531201),
            UserEvent(2, "post", 1672531202)
        )

        // Create temporary input file with test events
        tempInputFile.writeText(objectMapper.writeValueAsString(events))

        // Act
        eventAggregator.aggregateEvents(tempInputFile.absolutePath, tempOutputFile.absolutePath, update = false)

        // Assert
        val outputData = objectMapper.readValue<List<DailySummary>>(tempOutputFile)
        assertEquals(2, outputData.size)
        assertEquals(1, outputData[0].post)
        assertEquals(1, outputData[0].likeReceived)
        assertEquals(1, outputData[1].comment)
        assertEquals(1, outputData[1].post)
    }

    @Test
    fun `aggregateEvents with update should correctly update existing summary`() {
        // Arrange
        val initialSummary = listOf(
            DailySummary(1, "2023-01-01", 1),
            DailySummary(2, "2023-01-02")
        )

        // Create temporary output file with initial summary
        tempOutputFile.writeText(objectMapper.writeValueAsString(initialSummary))

        val newEvents = listOf(
            UserEvent(1, "post", 1672531200),
            UserEvent(2, "likeReceived", 1672531203)
        )

        // Create temporary input file with new events
        tempInputFile.writeText(objectMapper.writeValueAsString(newEvents))

        // Act
        eventAggregator.aggregateEvents(tempInputFile.absolutePath, tempOutputFile.absolutePath, update = true)

        // Assert
        val updatedSummary = objectMapper.readValue<List<DailySummary>>(tempOutputFile)
        assertEquals(3, updatedSummary.size)
        assertEquals(2, updatedSummary[0].post)
        assertEquals(1, updatedSummary[2].likeReceived)
    }

    @Test
    fun `readExistingOutput should return empty list for non-existing file`() {
        // Act
        val result = eventAggregator.readExistingOutput("nonExistingFile.json")

        // Assert
        assertEquals(emptyList<DailySummary>(), result)
    }

    @Test
    fun `readExistingOutput should return empty list for empty file`() {
        // Arrange
        tempOutputFile.writeText("")

        // Act
        val result = eventAggregator.readExistingOutput(tempOutputFile.absolutePath)

        // Assert
        assertEquals(emptyList<DailySummary>(), result)
    }

    @Test
    fun `readExistingOutput should correctly read existing summary from file`() {
        // Arrange
        val existingSummary = listOf(
            DailySummary(1, "2023-01-01"),
            DailySummary(2, "2023-01-02")
        )

        // Create temporary output file with existing summary
        tempOutputFile.writeText(objectMapper.writeValueAsString(existingSummary))

        // Act
        val result = eventAggregator.readExistingOutput(tempOutputFile.absolutePath)

        // Assert
        assertEquals(existingSummary, result)
    }

    @Test
    fun `readInputFile should return empty list for non-existing file`() {
        // Act
        val result = eventAggregator.readInputFile("nonExistingFile.json")

        // Assert
        assertEquals(emptyList<DailySummary>(), result)
    }

    @Test
    fun `readInputFile should return empty list for malformed JSON file`() {
        // Arrange
        val malformedJsonFile = createTempFile("malformedJson", ".json")

        // Act
        val result = eventAggregator.readInputFile(malformedJsonFile.absolutePath)

        // Assert
        assertEquals(emptyList<DailySummary>(), result)
    }

    @Test
    fun `readInputFile should correctly read user events from existing file`() {
        // Arrange
        val userEvents = listOf(
            UserEvent(1, "post", 1672444800),
            UserEvent(2, "likeReceived", 1672531201)
            // Add more test events as needed
        )

        // Create temporary input file with test events
        tempInputFile.writeText(objectMapper.writeValueAsString(userEvents))

        // Act
        val result = eventAggregator.readInputFile(tempInputFile.absolutePath)

        // Assert
        assertEquals(userEvents, result)
    }

    @Test
    fun `writeOutputFile should write data to the specified file`() {
        // Arrange
        val outputData = listOf(
            DailySummary(1, "2023-01-01", post = 2, likeReceived = 1),
            DailySummary(2, "2023-01-02", post = 1, comment = 1)
        )

        // Act
        eventAggregator.writeOutputFile(tempOutputFile.absolutePath, outputData)

        // Assert
        val writtenData = objectMapper.readValue<List<DailySummary>>(tempOutputFile)
        assertEquals(outputData, writtenData)
    }

    // Helper function to create a temporary file
    private fun createTempFile(prefix: String, suffix: String): File {
        return Files.createTempFile(prefix, suffix).toFile().apply { deleteOnExit() }
    }
}
