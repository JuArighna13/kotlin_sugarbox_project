package service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import dto.DailySummary
import dto.UserEvent
import java.io.File
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Service class for aggregating events and generating reports.
 */
class EventAggregator {
    private val objectMapper = ObjectMapper().registerKotlinModule()

    /**
     * Reads the existing output from the specified file.
     *
     * @param outputFileName The name of the file containing existing output data.
     * @return List of [DailySummary] objects representing existing output.
     */
    fun readExistingOutput(outputFileName: String): List<DailySummary> =
        try {
            objectMapper.readValue(File(outputFileName))
        } catch (e: FileNotFoundException) {
            println("Error: File not found - $outputFileName $e")
            emptyList()
        }catch (e: Exception) {
            println("Error reading from $outputFileName: ${e.message}")
            emptyList()
        }

    /**
     * Reads the input file containing user events.
     *
     * @param inputFileName The name of the file containing user events.
     * @return List of [UserEvent] objects representing user events.
     */
    fun readInputFile(inputFileName: String): List<UserEvent> =
        try {
            objectMapper.readValue(File(inputFileName))
        } catch (e: FileNotFoundException) {
            println("Error: File not found - $inputFileName")
            emptyList()
        } catch (e: Exception) {
            println("Error reading from $inputFileName: ${e.message}")
            emptyList()
        }

    /**
     * Writes the aggregated data to the specified output file in JSON format with pretty printing.
     *
     * @param outputFileName The name of the file to write the aggregated data.
     * @param outputData List of [DailySummary] objects representing the aggregated data.
     */
    fun writeOutputFile(outputFileName: String, outputData: List<DailySummary>) =
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(File(outputFileName), outputData)

    /**
     * Aggregates user events and generates a report, updating existing output if specified.
     *
     * @param inputFileName The name of the file containing user events.
     * @param outputFileName The name of the file to write the aggregated data.
     * @param update Indicates whether to update existing output or not.
     */
    fun aggregateEvents(inputFileName: String, outputFileName: String, update: Boolean) {
        val events: List<UserEvent> = readInputFile(inputFileName)
        val existingOutput: List<DailySummary> = if (update) readExistingOutput(outputFileName) else emptyList()

        // Dictionary to store aggregated data
        val userDailySummary = mutableMapOf<Int, MutableMap<String, DailySummary>>()

        // Include existing output in the aggregation
        existingOutput.forEach { entry ->
            userDailySummary
                .computeIfAbsent(entry.userId) { mutableMapOf() }
                .put(entry.date, entry)
        }

        // Process each event
        events.forEach { event ->
            val userId = event.userId
            val eventType = event.eventType
            val timestamp = event.timestamp

            // Convert timestamp to date
            val eventDate = SimpleDateFormat("yyyy-MM-dd").format(Date(timestamp * 1000))

            // Initialize user's daily summary if not exists
            userDailySummary
                .computeIfAbsent(userId) { mutableMapOf() }
                .computeIfAbsent(eventDate) { DailySummary(userId, eventDate) }
                .let { dailySummary ->
                    // Increment event type count dynamically
                    when (eventType) {
                        "post" -> dailySummary.post++
                        "likeReceived" -> dailySummary.likeReceived++
                        "comment" -> dailySummary.comment++
                    }
                }
        }

        // Convert the aggregated data to the desired output format
        val outputData = userDailySummary.values.flatMap { it.values }

        // Write the summary to the output JSON file
        writeOutputFile(outputFileName, outputData)
    }
}
